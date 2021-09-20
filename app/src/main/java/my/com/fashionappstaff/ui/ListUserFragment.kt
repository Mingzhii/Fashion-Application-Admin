package my.com.fashionappstaff.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.databinding.FragmentListUserBinding
import my.com.fashionappstaff.util.RewardAdapter
import my.com.fashionappstaff.util.UserAdapter
import my.com.fashionappstaff.util.snackbar

class ListUserFragment : Fragment() {

    private lateinit var binding: FragmentListUserBinding
    private val nav by lazy { findNavController() }
    private val vm: UserViewModel by activityViewModels()

    private lateinit var adapter: UserAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentListUserBinding.inflate(inflater, container, false)

        // TODO
        vm.search("")

        vm.getAll()

        binding.imgListUserBack.setOnClickListener { nav.navigate(R.id.action_listUserFragment_to_staffProfileFragment) }

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.edtSearchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.search(name)
                return true
            }
        })

        // Recycle Holder
        adapter = UserAdapter() { holder, user ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.updateUserFragment, bundleOf("id" to user.userId))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener {

                var auth : FirebaseAuth = FirebaseAuth.getInstance()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database

                        val user1 = auth.currentUser
                        user1?.delete()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    snackbar("user deleted successfully")
                                    delete(user.userId)
                                }
                            }
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

       vm.getResult().observe(viewLifecycleOwner) { list ->
           adapter.submitList(list)
           binding.txtItem.text = "${list.size} Staff(s)"
        }

        return binding.root
    }

    private fun delete(id: String) {
        // TODO: Delete
        vm.delete(id)
    }

}