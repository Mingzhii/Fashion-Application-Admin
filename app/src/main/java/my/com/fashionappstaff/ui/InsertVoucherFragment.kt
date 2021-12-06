package my.com.fashionappstaff.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Reward
import my.com.fashionappstaff.data.Voucher
import my.com.fashionappstaff.data.VoucherViewModel
import my.com.fashionappstaff.data.formatDate
import my.com.fashionappstaff.databinding.FragmentInsertVoucherBinding
import my.com.fashionappstaff.util.cropToBlob
import my.com.fashionappstaff.util.errorDialog
import my.com.fashionappstaff.util.informationDialog
import java.text.SimpleDateFormat
import java.util.*

class InsertVoucherFragment : Fragment() {

    private lateinit var binding: FragmentInsertVoucherBinding
    private val nav by lazy { findNavController() }
    private val vm: VoucherViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgVoucher.setImageURI(it.data?.data)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentInsertVoucherBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        vm.getAll()

        binding.imgVoucher.setOnClickListener { selectImage() }
        binding.btnVoucherInsert.setOnClickListener { submit() }
        binding.imgVoucherBack.setOnClickListener { nav.navigate(R.id.staffProfileFragment) }

        val formate = SimpleDateFormat("dd MMM, YYYY", Locale.US)

        binding.btnExpiryDate.setOnClickListener { it ->
            val currentDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(it.context , DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val date = formate.format(selectedDate.time)
                binding.btnExpiryDate.text = date
            },
                currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(
                    Calendar.DAY_OF_MONTH))
            datePicker.show()

        }

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun clear(){
        binding.imgVoucher.setImageResource(0)
        binding.edtVoucherName.editText?.setText("")
        binding.edtVoucherTermCondition.editText?.setText("")
        binding.edtVoucherDescription.editText?.setText("")
        binding.edtVoucherQuantity.editText?.setText("")
        binding.btnExpiryDate.text = "SELECT DATE"
        binding.edtVoucherValue.editText?.setText("")
    }

    private fun submit() {

        var voucherID = vm.validID()

        val v = Voucher(
            voucherId = voucherID,
            voucherImg = binding.imgVoucher.cropToBlob(300,300),
            voucherName = binding.edtVoucherName.editText?.text.toString().trim(),
            voucherTerm = binding.edtVoucherTermCondition.editText?.text.toString().trim(),
            voucherDescription = binding.edtVoucherDescription.editText?.text.toString().trim(),
            voucherQuantity = binding.edtVoucherQuantity.editText?.text.toString().toIntOrNull() ?: 0,
            voucherExpiryDate = binding.btnExpiryDate.text.toString(),
            voucherValue = binding.edtVoucherValue.editText?.text.toString().toDoubleOrNull() ?: 0.0,
        )

        val err = vm.validate(v)
        if (err != ""){
            errorDialog(err)
            return
        }else{
            val err = "Done"
            informationDialog(err)
            vm.set(v)
            clear()
        }

    }


}