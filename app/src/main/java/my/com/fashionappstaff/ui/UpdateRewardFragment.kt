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
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Product
import my.com.fashionappstaff.data.Reward
import my.com.fashionappstaff.data.RewardViewModel
import my.com.fashionappstaff.databinding.FragmentUpdateRewardBinding
import my.com.fashionappstaff.util.cropToBlob
import my.com.fashionappstaff.util.errorDialog
import my.com.fashionappstaff.util.toBitmap
import java.text.SimpleDateFormat
import java.util.*

class UpdateRewardFragment : Fragment() {

    private lateinit var binding: FragmentUpdateRewardBinding
    private val nav by lazy { findNavController() }
    private val vm: RewardViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id","N/A") }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgReward.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateRewardBinding.inflate(inflater, container, false)

        //TODO
        reset()
        binding.imgBack.setOnClickListener { nav.navigate(R.id.listRewardFragment) }
        binding.imgReward.setOnClickListener { select() }
        binding.btnSubmit.setOnClickListener { update() }

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

    private fun update() {

        val r = Reward(
            rewardID  = id,
            rewardName  = binding.edtRewardName.editText?.text.toString(),
            rewardDescrip = binding.edtRewardDescription.editText?.text.toString().trim(),
            rewardQuan    = binding.edtRewardQuantity.editText?.text.toString().toIntOrNull() ?: 0,
            rewardPoint   = binding.edtRewardPoint.editText?.text.toString().toIntOrNull() ?: 0,
            expiryDate    = binding.btnExpiryDate.text.toString(),
            rewardPhoto   = binding.imgReward.cropToBlob(300,300),
        )

        val err = vm.validate(r, false)

        if (err != ""){
            errorDialog(err)
            return
        }
        vm.set(r)
        nav.navigateUp()
    }

    private fun reset() {

        val r = vm.get(id)
        if (r == null){
            nav.navigateUp()
            return
        }
        load(r)
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }
    private fun load(r: Reward) {

        binding.imgReward.setImageBitmap(r.rewardPhoto.toBitmap())
        binding.edtRewardName.editText?.setText(r.rewardName)
        binding.edtRewardQuantity.editText?.setText(r.rewardQuan.toString())
        binding.edtRewardDescription.editText?.setText(r.rewardDescrip)
        binding.edtRewardPoint.editText?.setText(r.rewardPoint.toString())
        binding.btnExpiryDate.text = r.expiryDate

        binding.edtRewardName.requestFocus()
    }

}