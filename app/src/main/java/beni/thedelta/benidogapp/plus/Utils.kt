package beni.thedelta.benidogapp.plus

import android.Manifest
import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import beni.thedelta.benidogapp.R
import com.permissionx.guolindev.PermissionX
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


object Utils {

    fun permissionCheck(a: AppCompatActivity, function: (Boolean) -> Unit) {
        PermissionX.init(a)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    a.getString(R.string.permission_ration_msg),
                    a.getString(R.string.ok),
                    a.getString(R.string.cancel)
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    function.invoke(true)
                } else {
                    function.invoke(false)
                }
            }
    }

    fun saveImage(image: Bitmap, name: String, function: (Boolean) -> Unit): String? {
        var savedImagePath: String? = null
        val imageFileName = "$name.jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/BeniDogApi"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath

            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                function.invoke(false)
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath){
                function.invoke(true)
            }
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String, function: (Boolean) -> Unit) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(imagePath)
        val contentUri: Uri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        function.invoke(true)
    }


    fun animateToNormalSize(view: View, animListener: AnimListener? = null) {
        if (view.visibility != View.VISIBLE) view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .scaleY(1f)
            .scaleX(1f)
            .setDuration(300)
            .setInterpolator(OvershootInterpolator())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    animListener?.onAnimationEnd(p0)
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }

            }).start()
    }

    fun animateToScaledSize(
        view: View,
        scaleX: Float,
        scaleY: Float,
        animListener: AnimListener? = null
    ) {
        if (view.visibility != View.VISIBLE) view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .scaleY(scaleY)
            .scaleX(scaleX)
            .setDuration(300)
            .setInterpolator(OvershootInterpolator())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    animListener?.onAnimationEnd(p0)
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }

            }).start()
    }


    fun setStatusbarColorInt(context: Context, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            try {
                val activity = context as Activity
                var window = activity.window

                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

                // finally change the color
                window.setStatusBarColor(color)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun smoothlyChangeColor(view: View, fromColor: Int, toColor: Int, duration: Int) {

        val animator =
            ObjectAnimator.ofObject(view, "backgroundColor", ArgbEvaluator(), fromColor, toColor)
        animator.duration = duration.toLong()
        animator.start()
    }

    fun smoothlyChangeColor(
        view: View,
        @ColorRes fromColorRes: Int,
        @ColorRes toColorRes: Int,
        duration: String
    ) {

        val fromColor = view.resources.getColor(fromColorRes)
        val toColor = view.resources.getColor(toColorRes)
        smoothlyChangeColor(view, fromColor, toColor, Integer.valueOf(duration))
    }

    fun overlayDrawableColor(drawable: Drawable, @ColorInt color: Int) {
        var drawable = drawable
        if (color != 0) {
            drawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(drawable.mutate(), color)
        }
    }

    fun hideSoftInput(view: View) {
        view.clearFocus()
        val imm: InputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showSoftInput(view: View) {
        view.requestFocus()
        val imm: InputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    interface AnimListener {
        fun onAnimationEnd(anim: Animator?) {
        }
    }
}