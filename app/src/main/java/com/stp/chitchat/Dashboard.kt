package com.stp.chitchat

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stp.chitchat.databinding.FragmentDashboardBinding

class Dashboard : Fragment() {

    private lateinit var _binding: FragmentDashboardBinding
    private val binding get() = _binding
    private lateinit var appPermission: AppPermission
    private lateinit var mobileContacts: ArrayList<UserModel>
    private lateinit var appContacts: ArrayList<UserModel>


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            val action = DashboardDirections.actionDashboardToLogin()
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)


        appPermission = AppPermission()
        if (appPermission.isContactPermission(requireContext())) {
            getMobileContact()
        } else appPermission.requestContactPermission(requireActivity())


        return binding.root
    }

    private fun getMobileContact() {
        val projection = arrayOf(
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        if (cursor != null) {
            mobileContacts = ArrayList()
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var number =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                number = number.replace("\\s".toRegex(), "")
                val num = number.elementAt(0).toString()

                if (num == "0")
                    number = number.replaceFirst("(?:0)+".toRegex(), "+92")
                val userModel = UserModel()
                userModel.userName = name
                userModel.phoneNumber = number
                mobileContacts.add(userModel)
            }
            cursor.close()
            getAppContact(mobileContacts)
        }
    }

    private fun getAppContact(mobileContacts: ArrayList<UserModel>) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val query = databaseReference.orderByChild("phoneNumber")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    appContacts = ArrayList()
                    for (data in snapshot.children) {
                        val number = data.child("phoneNumber").value.toString()
                        for (mobileModel in mobileContacts) {
                            if (mobileModel.phoneNumber == number) {
                                val userModel = data.getValue(UserModel::class.java)
                                appContacts.add(userModel!!)
                            }
                        }
                    }
                    binding.contactsRecyclerViewList.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        setHasFixedSize(true)
                        adapter = ContactsAdapter(appContacts)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.action_bar_buttons, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_sign_out -> {
            auth.signOut()
            val action = DashboardDirections.actionDashboardToLogin()
            findNavController().navigate(action)
            true
        }

        R.id.action_profile -> {
            val action = DashboardDirections.actionDashboardToUserProfile()
            findNavController().navigate(action)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppConstants.CONTACT_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getMobileContact()
                else Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}