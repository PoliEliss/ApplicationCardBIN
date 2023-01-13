package com.rorono.applicationcardbin

import android.R
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.rorono.applicationcardbin.database.CardBINDataBase
import com.rorono.applicationcardbin.databinding.ActivityMainBinding
import com.rorono.applicationcardbin.network.RetrofitInstance
import com.rorono.applicationcardbin.repository.NetworkRepository
import com.rorono.applicationcardbin.repository.RepositoryDataBase
import com.rorono.applicationcardbin.viewmodel.CardViewModel
import com.rorono.applicationcardbin.viewmodel.CardViewModelFactory
import com.rorono.applicationcardbin.viewmodel.InfoCardState

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = NetworkRepository(retrofit = RetrofitInstance)
        val dataBase: CardBINDataBase = CardBINDataBase.getInstance(this)
        val repositoryDataBase = RepositoryDataBase(dataBase)
        val viewModelFactory = CardViewModelFactory(repository, repositoryDataBase)
        viewModel = ViewModelProvider(this, viewModelFactory)[CardViewModel::class.java]

        val binListDataBase = viewModel.getCardBINListDataBase()

        binding.ivSearch.setOnClickListener {
            val bin = binding.autoCompleteTVSearch.text.toString()
            viewModel.getCardInfo(bin)
        }

        var arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, binListDataBase)

        binding.autoCompleteTVSearch.setAdapter(arrayAdapter)

        viewModel.cardBINList.observe(this) {
            arrayAdapter =
                ArrayAdapter(this, R.layout.simple_list_item_1, it)
            binding.autoCompleteTVSearch.setAdapter(arrayAdapter)
        }

        viewModel.observeState(this) { state ->
            when (state) {
                InfoCardState.Loading -> {
                    Log.d("TEST", "загрузка")
                    binding.llCardInfo.visibility = View.INVISIBLE
                    binding.tvError.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is InfoCardState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.INVISIBLE
                    with(binding) {
                        llCardInfo.visibility = View.VISIBLE

                        viewModel.cardInfo.observe(this@MainActivity) {
                            it?.let {
                                tvScheme.text = it.scheme
                                tvType.text = it.type
                                tvBankName.text = it.bank?.name
                                tvBankURL.text = it.bank?.url
                                tvBankNumber.text = it.bank?.phone
                                tvBrand.text = it.brand
                                if (it.prepaid == false) {
                                    tvPrepaid.text =
                                        getString(com.rorono.applicationcardbin.R.string.no)
                                } else {
                                    tvPrepaid.text =
                                        getString(com.rorono.applicationcardbin.R.string.yes)
                                }
                                tvLength.text = it.number?.length.toString()
                                if (it.number?.luhn == false) {
                                    val lunchNo =
                                        getString(com.rorono.applicationcardbin.R.string.luhn) + getString(
                                            com.rorono.applicationcardbin.R.string.no
                                        )
                                    tvLuhn.text = lunchNo
                                } else {
                                    val lunchYes =
                                        getString(com.rorono.applicationcardbin.R.string.luhn) + getString(
                                            com.rorono.applicationcardbin.R.string.yes
                                        )
                                    tvLuhn.text = lunchYes
                                }
                                tvCountryEmoji.text = it.country?.emoji
                                tvCountry.text = it.country?.name
                                tvLatitude.text = it.country?.latitude?.toString()
                                tvLongitude.text = it.country?.longitude?.toString()
                            }
                            binding.tvBankURL.setOnClickListener {
                                val openURL = Intent(Intent.ACTION_VIEW)
                                openURL.data = Uri.parse("http://${binding.tvBankURL.text}")
                                startActivity(openURL)
                            }

                            binding.tvBankNumber.setOnClickListener {
                                val intent =
                                    Intent(
                                        Intent.ACTION_DIAL,
                                        Uri.parse("tel:" + binding.tvBankNumber.text)
                                    )
                                startActivity(intent)
                            }

                            binding.tvCountry.setOnClickListener {
                                openGoogleMap(
                                    binding.tvLatitude.text.toString().toDouble(),
                                    binding.tvLongitude.text.toString().toDouble(),
                                    this@MainActivity
                                )
                            }
                        }
                    }
                }
                is InfoCardState.Error -> {
                    binding.llCardInfo.visibility = View.INVISIBLE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = state.messageError
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}

private fun openGoogleMap(latitude: Double, longitude: Double, context: Context) {
    val uri = Uri.parse("geo:$latitude,$longitude?z=15")
    val mapIntent = Intent()

    mapIntent.action = Intent.ACTION_VIEW
    mapIntent.data = uri

    mapIntent.setPackage("com.google.android.apps.maps")
    try {
        context.startActivity(mapIntent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}