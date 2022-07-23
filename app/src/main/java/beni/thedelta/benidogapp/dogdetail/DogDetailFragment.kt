package beni.thedelta.benidogapp.dogdetail

import android.app.AlertDialog
import android.os.Bundle
import android.telephony.mbms.MbmsErrors
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import beni.thedelta.benidogapp.R
import beni.thedelta.benidogapp.breed.Breed
import beni.thedelta.benidogapp.databinding.FragmentDogDetailBinding
import beni.thedelta.benidogapp.dog.Dog
import beni.thedelta.benidogapp.plus.Utils

class DogDetailFragment : Fragment() {

    private lateinit var viewModel: DogDetailViewModel
    private lateinit var binding: FragmentDogDetailBinding
    private lateinit var dog: Dog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dog = Dog(
            arguments?.getString(Dog::class.java.simpleName),
            arguments?.getString(Breed::class.java.simpleName)
        )

        binding = FragmentDogDetailBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            if (binding.imgDog.scale != binding.imgDog.minimumScale)
                binding.imgDog.scale = binding.imgDog.minimumScale
            else {
                this.isEnabled = false
                activity?.onBackPressed()
            }

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        setHasOptionsMenu(true);

        viewModel = ViewModelProvider(this).get(DogDetailViewModel::class.java)


        // Observe for save image
        viewModel.saveViewState.observe(viewLifecycleOwner) {
            when (it) {
                is DogDetailViewState.Loading -> {
                    binding.progressLoad.visibility = View.VISIBLE
                    binding.progressLoad.start()
                }
                is DogDetailViewState.Success -> {
                    binding.progressLoad.visibility = View.GONE
                    binding.progressLoad.stop()
                    Toast.makeText(context, getString(R.string.save_success), Toast.LENGTH_SHORT)
                        .show()
                }
                is DogDetailViewState.Error -> {
                    binding.progressLoad.visibility = View.GONE
                    binding.progressLoad.stop()
                    Toast.makeText(context, getString(R.string.save_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        // Observe for share
        viewModel.shareViewState.observe(viewLifecycleOwner) {
            when (it) {
                is DogDetailViewState.Loading -> {
                    binding.progressLoad.visibility = View.VISIBLE
                    binding.progressLoad.start()
                }
                is DogDetailViewState.Success, is DogDetailViewState.Error -> {
                    binding.progressLoad.visibility = View.GONE
                    binding.progressLoad.stop()
                }
            }
        }

        binding.dog = dog

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dog_detail, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                save()
                true
            }
            R.id.action_share -> {
                share()
                true
            }
            else -> false
        }
    }


    fun save() {

        AlertDialog.Builder(context)
            .setTitle(R.string.save_title)
            .setPositiveButton(R.string.save_pos) { _, _ ->
                Utils.permissionCheck(activity as AppCompatActivity) { authorized ->

                    if (authorized) {
                        viewModel.savePhoto(dog.imageUrl!!)
                    } else {
                        Toast.makeText(context, getString(R.string.save_perm_error), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            .setNegativeButton(R.string.save_neg) { _, _ -> }
            .show()
    }

    fun share() {
        viewModel.sharePhoto(requireContext(), dog.imageUrl!!)
    }

}