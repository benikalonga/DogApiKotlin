package beni.thedelta.benidogapp.breed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import beni.thedelta.benidogapp.R
import beni.thedelta.benidogapp.databinding.FragmentBreedBinding

class BreedFragment : Fragment() {

    companion object {
        fun newInstance() = BreedFragment()
    }

    private lateinit var viewModel: BreedViewModel
    private lateinit var binding: FragmentBreedBinding
    private lateinit var adapter: BreedAdapter
    private lateinit var breeds: List<Breed>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBreedBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbarLayout.title = getString(R.string.app_name)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.load()
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = BreedAdapter(layoutInflater) {
            //On Item Clicked
            //Start Breed Fragment After One Second
            findNavController().navigate(
                R.id.action_to_DogFragment,
                bundleOf(Breed::class.java.simpleName to it.designation, "list" to it.list)
            )
        }
        initViews()

        viewModel = ViewModelProvider(this).get(BreedViewModel::class.java)
        viewModel.breedViewState.observe(viewLifecycleOwner) {

            when (it) {
                is BreedViewState.Loading -> {

                    binding.frameLoading.visibility = View.VISIBLE
                    binding.recyclerBreeds.visibility = View.GONE
                    binding.ivLoading.visibility = View.GONE
                    binding.tvLoading.text = getString(R.string.loading)
                    binding.refreshLayout.isRefreshing = true

                }
                is BreedViewState.Content -> {

                    adapter.submitList(it.breeds)
                    binding.frameLoading.visibility = View.GONE
                    binding.recyclerBreeds.visibility = View.VISIBLE
                    binding.ivLoading.visibility = View.GONE
                    binding.refreshLayout.isRefreshing = false

                }
                is BreedViewState.Error -> {
                    binding.refreshLayout.isRefreshing = false

                    binding.frameLoading.visibility = View.VISIBLE
                    binding.tvLoading.text = getString(R.string.error)
                    binding.ivLoading.visibility = View.VISIBLE

                    binding.recyclerBreeds.visibility = View.GONE
                }
            }
        }
        viewModel.load()

    }

    fun initViews() {
        binding.recyclerBreeds.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerBreeds.adapter = adapter
    }

}