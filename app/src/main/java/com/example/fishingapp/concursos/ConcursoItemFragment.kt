package com.example.fishingapp.concursos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.viewpager.widget.ViewPager
import com.example.fishingapp.R
import com.example.fishingapp.adapters.DemoCollectionPagerAdapter
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.databinding.FragmentConcursoItemBinding

class ConcursoItemFragment: Fragment() {

    private lateinit var binding: FragmentConcursoItemBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private lateinit var demoCollectionPagerAdapter: DemoCollectionPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_concurso_item,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        binding.concursoNombreDetail.text = model.getConcursoDetail().nombre
        binding.concursoPremioDetail.text = model.getConcursoDetail().premio

        demoCollectionPagerAdapter = DemoCollectionPagerAdapter(childFragmentManager)
        binding.viewPager?.adapter = demoCollectionPagerAdapter
        binding.concursoBasesAndCondicionesDetail
        binding.tabView?.setupWithViewPager(binding.viewPager)
        return view
    }
}

