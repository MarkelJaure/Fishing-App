package com.example.fishingapp.concursos

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.adapters.RankingAdapter
import com.example.fishingapp.databinding.FragmentMapsBinding
import com.example.fishingapp.databinding.FragmentRankingListBinding
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReglamentacionViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlin.math.max
import kotlin.math.min

class RankingListFragment : Fragment() {

    private lateinit var binding: FragmentRankingListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ranking_list,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        val rankingList: RecyclerView = binding.list

        val rankingAdapter = RankingAdapter()
        rankingList.adapter = rankingAdapter
        rankingAdapter.rankings = model.getConcursoDetail().ranking.ranking

        return view
    }
}

