package com.example.listycity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);

        void editCity(int position, City updatedCity);
    }

    private AddCityDialogListener listener;

    private static final String ARG_CITY = "city";
    private static final String ARG_POS = "position";

    public static AddCityFragment newInstance(City city, int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        args.putInt(ARG_POS, position);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);


        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        Bundle args = getArguments();
        boolean isEdit = (args != null && args.containsKey(ARG_CITY) && args.containsKey(ARG_POS));

        int position = -1;
        if (isEdit) {
            City city = (City) args.getSerializable(ARG_CITY);
            position = args.getInt(ARG_POS);

            if (city != null) {
                editCityName.setText(city.getName());
                editProvinceName.setText(city.getProvince());
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        int finalPosition = position;


        return builder
                .setView(view)
                .setTitle(isEdit ? "Add/edit city" : "Add a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEdit ? "OK" : "Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (isEdit) {
                        listener.editCity(finalPosition, new City(cityName, provinceName));
                    } else {
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}