package my.com.fashionappstaff.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Product
import my.com.fashionappstaff.databinding.FragmentInsertProductBinding
import my.com.fashionappstaff.util.SpinnerExtension
import my.com.fashionappstaff.util.cropToBlob
import my.com.fashionappstaff.util.errorDialog

class InsertProductFragment : Fragment() {

    private lateinit var binding: FragmentInsertProductBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgProduct.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInsertProductBinding.inflate(inflater, container, false)

        // TODO Insert Product

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        vm.getAll()

        binding.imgProduct.setOnClickListener { selectImage() }
        binding.btnDone.setOnClickListener { submit() }
        binding.imgInsertProductBack.setOnClickListener { nav.navigate(R.id.action_insertProductFragment_to_productFragment) }

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun submit() {
        var category = binding.spCategory.selectedItem.toString()

        val id = "PROD00" + (vm.calSize() + 1).toString()
        val p = Product(
            productId = id,
            productName = binding.edtProductName.editText?.text.toString().trim(),
            productDescrip = binding.edtProductDescrip.editText?.text.toString().trim(),
            productQuan = binding.edtProductQuantity.editText?.text.toString().toIntOrNull() ?: 0,
            productPrice = binding.edtProductPrice.editText?.text.toString().toDoubleOrNull() ?: 0.0,
            productCategory = category,
            productPhoto = binding.imgProduct.cropToBlob(300,300),
        )

        val err = vm.validate(p)
        if (err != ""){
            errorDialog(err)
            return
        }else{
            val err = "Done"
            errorDialog(err)
            vm.set(p)
        }


    }



}