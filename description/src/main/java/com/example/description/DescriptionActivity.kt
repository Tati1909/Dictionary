package com.example.description

import android.content.Context
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.description.databinding.ActivityDescriptionBinding
import com.example.utils.network.OnlineLiveData
import com.example.utils.ui.AlertDialogFragment

/**
 *Экран с описанием слова и картинкой.
 */
class DescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescriptionBinding

    @RequiresApi(31)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionbarHomeButtonAsUp()
        binding.descriptionScreenSwipeRefreshLayout.setOnRefreshListener {
            startLoadingOrShowError()
        }
        setData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setActionbarHomeButtonAsUp() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @RequiresApi(31)
    private fun setData() {
        val bundle = intent.extras
        binding.descriptionHeader.text = bundle?.getString(WORD_EXTRA)
        binding.descriptionTextview.text = bundle?.getString(DESCRIPTION_EXTRA)
        val imageLink = bundle?.getString(URL_EXTRA)
        if (imageLink.isNullOrBlank()) {
            stopRefreshAnimationIfNeeded()
        } else {
            useCoilToLoadPhoto(binding.descriptionImageview, imageLink)
        }
    }

    @RequiresApi(31)
    private fun startLoadingOrShowError() {
        OnlineLiveData(this).observe(
            this@DescriptionActivity
        ) {
            if (it) {
                setData()
            } else {
                AlertDialogFragment.newInstance(
                    getString(R.string.dialog_title_device_is_offline),
                    getString(R.string.dialog_message_device_is_offline)
                ).show(
                    supportFragmentManager,
                    DIALOG_FRAGMENT_TAG
                )
                stopRefreshAnimationIfNeeded()
            }
        }
    }

    private fun stopRefreshAnimationIfNeeded() {
        if (binding.descriptionScreenSwipeRefreshLayout.isRefreshing) {
            binding.descriptionScreenSwipeRefreshLayout.isRefreshing = false
        }
    }

    /**
     * Для того, чтобы обрабатывать ошибки, нам нужно сформировать ImageRequest, который создается через Builder,
     * где мы передаем ссылку на фото и листенер с тремя колбэками:
    1. В onStart мы можем сделать что-то непосредственно перед началом загрузки;
    2. В onSuccess мы получаем готовый drawable;
    3. В onError мы можем как-то обработать ошибку.
    Готовый запрос мы выполняем через вспомогательный класс библиотеки Coil ImageLoader, который
    выполняет запросы на загрузку.
     */
    @RequiresApi(31)
    private fun useCoilToLoadPhoto(imageView: ImageView, imageLink: String) {

        val request = ImageRequest.Builder(this)
            .data("https:$imageLink")
            .target(
                onStart = {},
                onSuccess = { result ->
                    imageView.setImageDrawable(result)
                    /**
                     * эффект размытия
                     */
                    val blurEffect = RenderEffect.createBlurEffect(16f, 16f, Shader.TileMode.MIRROR)
                    imageView.setRenderEffect(blurEffect)
                },
                onError = { imageView.setImageResource(R.drawable.ic_load_error_vector) }
            )
            //.transformations(
            //    CircleCropTransformation(),
            //)
            .build()

        ImageLoader(this).enqueue(request)
    }

    companion object {

        private const val DIALOG_FRAGMENT_TAG = "8c7dff51-9769-4f6d-bbee-a3896085e76e"

        private const val WORD_EXTRA = "f76a288a-5dcc-43f1-ba89-7fe1d53f63b0"
        private const val DESCRIPTION_EXTRA = "0eeb92aa-520b-4fd1-bb4b-027fbf963d9a"
        private const val URL_EXTRA = "6e4b154d-e01f-4953-a404-639fb3bf7281"

        fun getIntent(
            context: Context,
            word: String,
            description: String,
            url: String?
        ): Intent = Intent(context, DescriptionActivity::class.java).apply {
            putExtra(WORD_EXTRA, word)
            putExtra(DESCRIPTION_EXTRA, description)
            putExtra(URL_EXTRA, url)
        }
    }
}