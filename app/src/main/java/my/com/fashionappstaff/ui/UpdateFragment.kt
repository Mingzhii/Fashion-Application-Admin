package my.com.fashionappstaff.ui

import android.app.Activity
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.Blob
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Product
import my.com.fashionappstaff.databinding.FragmentUpdateBinding
import my.com.fashionappstaff.util.cropToBlob
import my.com.fashionappstaff.util.errorDialog
import my.com.fashionappstaff.util.toBitmap
import java.util.*


class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id","N/A") }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgProductPict.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        //TODO
        reset()
        binding.imgUpdateProductBack.setOnClickListener { nav.navigate(R.id.action_updateFragment_to_listProductFragment) }
        binding.imgProductPict.setOnClickListener { select() }
        binding.btnProductUpdateSubmit.setOnClickListener { update() }


        return binding.root
    }

    private fun reset() {

        val p = vm.get(id)
        if (p == null){
            nav.navigateUp()
            return
        }
        load(p)
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun load(p: Product) {

        binding.imgProductPict.setImageBitmap(p.productPhoto.toBitmap())
        binding.edtProductName.editText?.setText(p.productName)
        binding.edtProductQuantity.editText?.setText(p.productQuan.toString())
        binding.edtProductDescrip.editText?.setText(p.productDescrip)
        binding.edtProductPrice.editText?.setText("%.2f".format(p.productPrice))

        val position = check(p.productCategory)
        binding.spCategory.setSelection(position)

        binding.edtProductName.requestFocus()
    }

    private fun check(productCategory: String): Int {

        if (productCategory == "Women"){
            return 0
        }else {
            return 1
        }
    }

    private fun update() {
        // TODO: Update (set)
        val p = Product(
            productId = id,
            productName = binding.edtProductName.editText?.text.toString(),
            productDescrip = binding.edtProductDescrip.editText?.text.toString(),
            productQuan = binding.edtProductQuantity.editText?.text.toString().toIntOrNull() ?: 0,
            productPrice = binding.edtProductPrice.editText?.text.toString().toDoubleOrNull() ?: 0.0,
            productCategory = binding.spCategory.selectedItem.toString(),
            productPhoto = binding.imgProductPict.cropToBlob(300,300),
        )

//        val err = vm.validate(p, false)
//        if (err != ""){
//            errorDialog(err)
//            return
//        }

        vm.set(p)
        nav.navigateUp()
    }

}