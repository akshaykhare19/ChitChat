package com.stp.chitchat

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stp.chitchat.databinding.FragmentUserProfileBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class UserProfile : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var appPermission: AppPermission

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        appPermission = AppPermission()
        auth = FirebaseAuth.getInstance()
        sharedPreferences = requireContext()
            .getSharedPreferences("userData", Context.MODE_PRIVATE)

        profileViewModel = ViewModelProvider
            .AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(ProfileViewModel::class.java)

        profileViewModel.getUser().observe(viewLifecycleOwner, Observer {
            binding!!.userDataModel = it

            binding!!.name.text = it.userName
        })

        binding!!.addPhotoBtn.setOnClickListener {
            if(appPermission.isStorageOk(requireContext())) pickImage()
            else appPermission.requestStoragePermission(requireActivity())
        }

        binding!!.name.setOnClickListener {
            getUserNameEditDialog()
        }

        binding!!.bio.setOnClickListener {
            getUserBioEditDialog()
        }

//
//        val requestPermissionLauncher =
//            registerForActivityResult(
//                ActivityResultContracts.RequestPermission()
//            ) { isGranted: Boolean ->
//                if (isGranted) {
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//                    pickImage()
//                } else {
//                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
//                    // Explain to the user that the feature is unavailable because the
//                    // features requires a permission that the user has denied. At the
//                    // same time, respect the user's decision. Don't link to system
//                    // settings in an effort to convince the user to change their
//                    // decision.
//                }
//            }
//
//        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        return binding?.root
    }

    private fun uploadImage(imageUri: Uri) {
        storageReference = FirebaseStorage.getInstance().reference
        storageReference.child(auth.uid + AppConstants.PATH).putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                val task = taskSnapshot.storage.downloadUrl
                task.addOnCompleteListener {
                    if(it.isSuccessful) {
                        val imagePath = it.result.toString()
                        val editor = sharedPreferences.edit()
                        editor.putString("myImage", imagePath).apply()

                        profileViewModel.updateUserImage(imagePath)
                    }
                }
            }
    }

    private fun getUserBioEditDialog() {
        val builder = AlertDialog.Builder(requireContext())

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setTitle("Edit Bio")
        builder.setMessage("Enter your bio here")
        builder.setPositiveButton("Confirm") { dialog, _ ->
            if(input.text.isNotEmpty()) {
                profileViewModel.updateUserBio(input.text.toString())
            }
            Toast.makeText(requireContext(), "Bio updated successfully", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }


        //to avoid removal of dialog box on clicking outside the dialog UI
        builder.setCancelable(false)

        val defaultDialog = builder.create()
        defaultDialog.show()

    }

    private fun pickImage() {
        CropImage.activity().setCropShape(CropImageView.CropShape.OVAL)
            .start(requireContext(), this)
    }

    private fun getUserNameEditDialog() {
        val builder = AlertDialog.Builder(requireContext())

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setTitle("Edit Name")
        builder.setMessage("Enter your name here")
        builder.setPositiveButton("Confirm") { dialog, _ ->
            if(input.text.isNotEmpty()) {
                profileViewModel.updateUserName(input.text.toString())
            }
            Toast.makeText(requireContext(), "Username changed to ${input.text}!", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }


        //to avoid removal of dialog box on clicking outside the dialog UI
        builder.setCancelable(false)

        val defaultDialog = builder.create()
        defaultDialog.show()

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode) {
//            100 -> {
//                if(data != null) {
//
//                }
//            }
//        }
//    }


}