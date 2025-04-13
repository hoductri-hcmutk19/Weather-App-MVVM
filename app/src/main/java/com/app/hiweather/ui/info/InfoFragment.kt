package com.app.hiweather.ui.info

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.os.bundleOf
import com.app.hiweather.BuildConfig
import com.app.hiweather.R
import com.app.hiweather.base.BaseFragment
import com.app.hiweather.databinding.FragmentInfoBinding
import com.app.hiweather.ui.SharedViewModel
import com.app.hiweather.ui.home.WeatherViewModel
import com.app.hiweather.utils.goBackFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoFragment : BaseFragment<FragmentInfoBinding>(FragmentInfoBinding::inflate) {

    override val viewModel: WeatherViewModel by viewModel()
    override val sharedViewModel: SharedViewModel by activityViewModel()

    override fun initView() {
        binding.layoutHeader.icBack.setOnClickListener {
            goBackFragment()
        }
        binding.ratingLayout.setOnClickListener {
            // TODO implement later
        }
        binding.shareLayout.setOnClickListener {
            val appPackageName = requireContext().packageName
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.share_message, appPackageName)
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua / Share via"))
        }
        binding.contactLayout.setOnClickListener {
            val email = "toitentri2001@gmail.com"
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_support))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
            }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_app_mail_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.otherLayout.setOnClickListener {
            // TODO implement later
        }
        binding.conditionsLayout.setOnClickListener {
            val termConditionUrl = "https://www.freeprivacypolicy.com/live/2fcf7858-10e3-4f93-b586-d4672b5b0892"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termConditionUrl))
            startActivity(intent)
        }
        binding.privacyLayout.setOnClickListener {
            val privacyPolicyUrl = "https://www.freeprivacypolicy.com/live/b99831a8-af25-400b-9618-64cc8b3436b8"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
            startActivity(intent)
        }
    }

    override fun initData() {}

    override fun bindData() {
        binding.tvVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }

    companion object {
        fun newInstance() =
            InfoFragment().apply {
                arguments = bundleOf()
            }
    }
}
