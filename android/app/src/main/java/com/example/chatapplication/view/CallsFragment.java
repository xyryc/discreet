package com.example.chatapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.CallListAdapter;
import com.example.chatapplication.databinding.FragmentCallsBinding;
import com.example.chatapplication.view.contact.ContactsActivity;
import com.example.chatapplication.viewmodel.CallsViewModel;

import java.util.ArrayList;

public class CallsFragment extends Fragment {

    private FragmentCallsBinding binding;
    private CallsViewModel viewModel;
    private CallListAdapter callListAdapter;
    private boolean isMissedOnlyFilter = false;

    public CallsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCallsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CallsViewModel.class);

        setupCallsRecycler();
        observeViewModel();
        setupFilterChips();
        initClickActions();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refresh();
        }
    }

    private void setupCallsRecycler() {
        binding.recyclerViewCalls.setLayoutManager(new LinearLayoutManager(getContext()));
        callListAdapter = new CallListAdapter(new ArrayList<>(), getContext());
        binding.recyclerViewCalls.setAdapter(callListAdapter);
    }

    private void observeViewModel() {
        viewModel.getCallHistory().observe(getViewLifecycleOwner(), calls -> {
            if (calls != null) {
                callListAdapter.updateList(calls);
                if (isMissedOnlyFilter) {
                    callListAdapter.filterMissedOnly(true);
                }
                binding.layoutEmptyState.setVisibility(callListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setupFilterChips() {
        binding.chipFilterAll.setOnClickListener(v -> {
            isMissedOnlyFilter = false;
            setChipActive(binding.chipFilterAll, "All Calls");
            setChipInactive(binding.chipFilterMissed, "Missed");
            if (callListAdapter != null) {
                callListAdapter.filterMissedOnly(false);
                binding.layoutEmptyState.setVisibility(callListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        binding.chipFilterMissed.setOnClickListener(v -> {
            isMissedOnlyFilter = true;
            setChipActive(binding.chipFilterMissed, "Missed");
            setChipInactive(binding.chipFilterAll, "All Calls");
            if (callListAdapter != null) {
                callListAdapter.filterMissedOnly(true);
                binding.layoutEmptyState.setVisibility(callListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setChipActive(TextView chip, String text) {
        if (getContext() == null) return;
        chip.setBackgroundResource(R.drawable.bg_neu_chip_active);
        chip.setTextColor(getResources().getColor(R.color.neu_text_on_accent));
        chip.setPadding(dpToPx(14), dpToPx(8), dpToPx(14), dpToPx(8));
        chip.setText(text);
    }

    private void setChipInactive(TextView chip, String text) {
        if (getContext() == null) return;
        chip.setBackgroundResource(R.drawable.bg_neu_chip_inactive);
        chip.setTextColor(getResources().getColor(R.color.neu_text_secondary));
        chip.setPadding(dpToPx(14), dpToPx(8), dpToPx(14), dpToPx(8));
        chip.setText(text);
    }

    private int dpToPx(int dp) {
        if (getContext() == null) return dp;
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void initClickActions() {
        // Create Call Link Hero Card
        binding.cardCreateCallLink.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Join my encrypted Discreet room: https://discreet.chat/call/room-748291");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Discreet Call Link"));
        });

        // New Call Header Button
        binding.btnHeaderNewCall.setOnClickListener(v -> 
            startActivity(new Intent(getContext(), ContactsActivity.class))
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
