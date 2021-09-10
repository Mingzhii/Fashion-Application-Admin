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
import com.google.firebase.firestore.Blob
import kotlinx.coroutines.selects.select
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Reward
import my.com.fashionappstaff.data.RewardViewModel
import my.com.fashionappstaff.databinding.FragmentInsertRewardBinding
import my.com.fashionappstaff.util.cropToBlob
import my.com.fashionappstaff.util.errorDialog
import my.com.fashionappstaff.util.snackbar
import java.text.SimpleDateFormat
import java.util.*

class InsertRewardFragment : Fragment() {

    private lateinit var binding: FragmentInsertRewardBinding
    private val nav by lazy { findNavController() }
    private val vm: RewardViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgRewardPic.setImageURI(it.data?.data)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentInsertRewardBinding.inflate(inflater, container, false)

        //TODO
        vm.getAll()

        binding.imgRewardPic.setOnClickListener { selectImage() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.imgBack.setOnClickListener { nav.navigateUp() }

        val formate = SimpleDateFormat("dd MMM, YYYY", Locale.US)

        binding.btnExpiryDate.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(it.context , DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val date = formate.format(selectedDate.time)
                binding.btnExpiryDate.text = date
            },
                currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun submit() {

        val id = "REW00" + (vm.calSize() + 1).toString()
        val r = Reward(
            rewardID = id,
            rewardName = binding.edtRewardName.editText?.text.toString().trim(),
            rewardDescrip = binding.edtRewardDescription.editText?.text.toString().trim(),
            rewardQuan    = binding.edtRewardQuantity.editText?.text.toString().toIntOrNull() ?: 0,
            rewardPoint   = binding.edtRewardPoint.editText?.text.toString().toIntOrNull() ?: 0,
            expiryDate    = binding.btnExpiryDate.text.toString(),
            rewardPhoto   = binding.imgRewardPic.cropToBlob(300,300),
        )

        val err = vm.validate(r)
        if (err != ""){
            errorDialog(err)
            return
        }else{
            val err = "Done"
            errorDialog(err)
            vm.set(r)
        }

    }

}