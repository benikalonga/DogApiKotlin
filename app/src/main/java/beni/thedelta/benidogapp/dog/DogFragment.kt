package beni.thedelta.benidogapp.dog

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
import beni.thedelta.benidogapp.breed.Breed
import beni.thedelta.benidogapp.databinding.FragmentDogsBinding

class DogFragment : Fragment() {

    private lateinit var viewModel: DogViewModel
    private lateinit var binding: FragmentDogsBinding
    private var adapter: DogAdapter? =null
    private lateinit var breed: Breed
    private lateinit var dogs: List<Dog>
    private var viewFrag: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (viewFrag == null){
            breed = Breed(
                arguments?.getString(Breed::class.java.simpleName)!!,
                arguments?.getString("list")!!
            )

            binding = FragmentDogsBinding.inflate(inflater, container, false).apply {
                viewFrag = root
            }

            binding.refreshLayout.setOnRefreshListener {
                viewModel.load(breed.designation)
            }
        }

        return viewFrag as View

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (adapter == null){

            binding.toolbar.title = breed.designation

            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

            adapter = DogAdapter(layoutInflater) { _, _, dog ->
                findNavController().navigate(
                    R.id.action_to_DogDetail, bundleOf(
                        Breed::class.java.simpleName to dog.breed,
                        Dog::class.java.simpleName to dog.imageUrl
                    )
                )
            }
            initViews()

            viewModel = ViewModelProvider(this).get(DogViewModel::class.java)
            viewModel.dogViewState.observe(viewLifecycleOwner) {

                when (it) {
                    is DogViewState.Loading -> {

                        binding.frameLoading.visibility = View.VISIBLE
                        binding.recyclerDogs.visibility = View.GONE
                        binding.ivLoading.visibility = View.GONE
                        binding.tvLoading.text = getString(R.string.loading)
                        binding.refreshLayout.isRefreshing = true

                    }
                    is DogViewState.Content -> {
                        dogs = it.dogs

                        infiniteLoading()

                        binding.frameLoading.visibility = View.GONE
                        binding.recyclerDogs.visibility = View.VISIBLE
                        binding.ivLoading.visibility = View.GONE
                        binding.refreshLayout.isRefreshing = false

                    }
                    is DogViewState.Error -> {
                        binding.refreshLayout.isRefreshing = false

                        binding.frameLoading.visibility = View.VISIBLE
                        binding.tvLoading.text = getString(R.string.error)
                        binding.ivLoading.visibility = View.VISIBLE

                        binding.recyclerDogs.visibility = View.GONE
                    }
                }
            }
            viewModel.load(breed.designation)
        }
    }

    fun initViews() {
        binding.recyclerDogs.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerDogs.adapter = adapter

        binding.recyclerDogs.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.recyclerDogs.canScrollVertically(1)) {
                    infiniteLoading()
                }
            }
        })

    }

    fun infiniteLoading() {

        val max = dogs.size
        val curList = adapter!!.currentList
        val curSize = curList.size
        val nextSize = if (curSize + 10 < max) curSize + 10 else max

        if (max == curSize)
            return

        binding.progressLoad.visibility = View.VISIBLE
        binding.progressLoad.start()

        val newList = mutableListOf<Dog>().apply {
            addAll(curList)
            addAll(dogs.subList(curSize, nextSize))
        }

        // Log.i("Populate", "Max $max, CurSize $curSize, NextSize $nextSize")

        binding.progressLoad.postDelayed({
            binding.progressLoad.visibility = View.GONE
            binding.progressLoad.stop()
            adapter!!.submitList(newList)
            binding.recyclerDogs.smoothScrollBy(0,20)
        }, 1000)
    }
}