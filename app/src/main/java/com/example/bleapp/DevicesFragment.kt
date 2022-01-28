package com.example.bleapp

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bleapp.databinding.FragmentDevicesBinding


class DevicesFragment : Fragment(), DevicesAdapter.Callback {
    private var _binding: FragmentDevicesBinding? = null
    private val binding: FragmentDevicesBinding get() = _binding!!

    private val devicesAdapter = DevicesAdapter()

    private val viewModel: DevicesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDevicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDevices.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = devicesAdapter
        }

        binding.fabStartScan.setOnClickListener {
            checkLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onStart() {
        super.onStart()
        subscribeOnViewModel()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopScan()
    }

    private fun subscribeOnViewModel() {
        viewModel.devices.observe(viewLifecycleOwner, { devices ->
            devicesAdapter.update(devices)
        })
    }


    private val checkLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                viewModel.startScan()
            }
        }

    override fun onItemClick(device: BluetoothDevice) {
        // todo transaction to other fragment
    }

    companion object {
        fun newInstance() = DevicesFragment()
    }
}