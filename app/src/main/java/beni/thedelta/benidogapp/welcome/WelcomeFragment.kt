package beni.thedelta.benidogapp.welcome

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import beni.thedelta.benidogapp.MainActivity
import beni.thedelta.benidogapp.databinding.FragmentWelcomeBinding
import beni.thedelta.benidogapp.plus.Utils
import jp.wasabeef.blurry.Blurry

class WelcomeFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    private lateinit var viewModel: WelcomeViewModel
    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)

        animate()
    }

    private fun hide() {
        // Hide UI first
        if (Build.VERSION.SDK_INT >= 30) {
            binding.fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            binding.fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    override fun onResume() {
        super.onResume()
        hide()
    }

    fun animate() {

        binding.fullscreenContent.postDelayed({

            Utils.animateToScaledSize(binding.linearHome, 0.8f, 0.8f, object : Utils.AnimListener {
                override fun onAnimationEnd(p0: Animator?) {
                    super.onAnimationEnd(p0)
                    Blurry.with(activity)
                        .radius(10)
                        .sampling(2)
                        .async()
                        .color(Color.argb(66, 160, 160, 160))
                        .capture(binding.linearHome)
                        .getAsync {
                            binding.frameBlur.setImageDrawable(BitmapDrawable(resources, it))
                            binding.linearHome.visibility = View.GONE
                            binding.frameTrans.visibility = View.VISIBLE
                            binding.ivTheDelta.visibility = View.VISIBLE
                            binding.ivTheDelta.scaleX = 1.2f
                            binding.ivTheDelta.scaleY = 1.2f
                            Utils.animateToNormalSize(binding.ivTheDelta)
                            binding.ivTheDelta.postDelayed({
                                //Start Breed Fragment After One Second
                                activity?.startActivity(Intent(context, MainActivity::class.java))
                                activity?.finish()
                            }, 1000)
                        }
                }
            })
        }, 2000)

    }

}