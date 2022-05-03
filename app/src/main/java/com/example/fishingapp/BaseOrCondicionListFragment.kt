package com.example.fishingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.adapters.BaseOrCondicionAdapter
import com.example.fishingapp.databinding.FragmentBaseOrCondicionListBinding
import com.example.fishingapp.models.BaseOrCondicion
import com.example.fishingapp.viewModels.MyViewModel

class BaseOrCondicionListFragment : Fragment() {

    private lateinit var binding: FragmentBaseOrCondicionListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_base_or_condicion_list,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        val basesOrCondicionesList: RecyclerView = binding.list

        val baseOrCondicionAdapter = BaseOrCondicionAdapter()
        basesOrCondicionesList.adapter = baseOrCondicionAdapter
        baseOrCondicionAdapter.basesOrCondiciones = model.getConcursoDetail()!!.basesAndCondiciones

        return view
    }
}