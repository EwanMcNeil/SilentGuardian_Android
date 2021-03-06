package com.example.silentguardian_android.Fragments;


import android.content.ContentResolver;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.Helpers.DatabaseHelper;
import com.example.silentguardian_android.Helpers.Person;
import com.example.silentguardian_android.Helpers.SharePreferenceHelper;
import com.example.silentguardian_android.R;
import com.example.silentguardian_android.Activities.thresholdActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class loadCellPhoneContact_fragment extends DialogFragment {

    ArrayList<Person> mainAndroidPersonList = new ArrayList<>();
    protected ListView androidContactListview;
    protected Button closeButton;
    protected TextView androidContactTV;

    protected SharePreferenceHelper sharePreferenceHelper;


    protected Button manualAdd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.import_contact_from_phone, container, false);
        ArrayList<Person> androidPersonList = new ArrayList<>();
        //Next lines assure activity uses the right language, otherwise some activities or fragment aren't fully catching up
        //Applying language start
        sharePreferenceHelper = new SharePreferenceHelper(getContext());
        String language = sharePreferenceHelper.languageReturn();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        //Applying language end
        androidContactListview = view.findViewById(R.id.cellPhoneContactLV);
        closeButton = view.findViewById(R.id.closeButtonAndroidContact);
        androidContactTV = view.findViewById(R.id.androidListTV);
        manualAdd = view.findViewById(R.id.manualaddcontactButton);

        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            String contactPhone = "";
            String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (hasPhoneNumber > 0) {

                Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactID}, null);

                while (phoneCursor.moveToNext()) {
                    contactPhone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                }
                phoneCursor.close();
            }
            Person tempPerson = new Person(contactName, contactPhone);
            androidPersonList.add(tempPerson);
        }
        mainAndroidPersonList = androidPersonList;
        loadAndroidContactListView();

        manualAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertContactDialogFragment dialog = new insertContactDialogFragment();
                dialog.show(getActivity().getSupportFragmentManager(), "insertContactFragment");
            }
        });

        androidContactListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();

                Person selectedPerson = new Person(mainAndroidPersonList.get(position).getName(), mainAndroidPersonList.get(position).getPhoneNumber(), 0, 0);

                DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
                List<Person> currentContacts = dbhelper.getAllPeople();
                for (Person n : currentContacts) {
                    Log.d("__insertFrag", n.getName());

                    if (n.equals(selectedPerson)) {

                        Toast.makeText(getContext(), R.string.contactexist, Toast.LENGTH_LONG).show();
                        return;//do not insert
                    }
                }

                dbhelper.insertPerson(selectedPerson);
                Toast.makeText(getContext(), R.string.contactAdded, Toast.LENGTH_LONG).show();
                ((thresholdActivity) getActivity()).loadContactsListView();


            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbhelper = new DatabaseHelper(getActivity());

                if (dbhelper.checkifEmpty()) {
                    SharePreferenceHelper helper = new SharePreferenceHelper(getContext());
                    if (!helper.getTutorialSeen()) {
                        ((thresholdActivity) getActivity()).loadThresholdMode();
                    }

                    getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), R.string.pleaseAddContact, Toast.LENGTH_LONG).show();
                }


            }
        });
        return view;
    }

    public void loadAndroidContactListView() {

        ArrayList<String> contactListText = new ArrayList<>();

        for (int i = 0; i < mainAndroidPersonList.size(); i++) {
            String temp = "";
            temp += mainAndroidPersonList.get(i).getName() + '\n';
            temp += mainAndroidPersonList.get(i).getPhoneNumber() + '\n';
            contactListText.add(temp);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contactListText);
        androidContactListview.setAdapter(arrayAdapter);
    }
}
