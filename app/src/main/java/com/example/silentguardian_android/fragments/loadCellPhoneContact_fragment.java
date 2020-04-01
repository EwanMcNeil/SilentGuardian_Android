package com.example.silentguardian_android.fragments;



import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.silentguardian_android.Database.DatabaseHelper;
import com.example.silentguardian_android.Database.Person;
import com.example.silentguardian_android.R;

import java.util.ArrayList;
import java.util.List;

public class loadCellPhoneContact_fragment extends DialogFragment {

    ArrayList<Person> mainAndroidPersonList = new ArrayList<>();
    protected ListView androidContactListview;
    protected Button closeButton;
    protected TextView androidContactTV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.import_contact_from_phone, container, false);
        ArrayList<Person> androidPersonList = new ArrayList<>();
        androidContactListview = view.findViewById(R.id.cellPhoneContactLV);
        closeButton = view.findViewById(R.id.closeButtonAndroidContact);
        androidContactTV = view.findViewById(R.id.androidListTV);
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while(cursor.moveToNext()){
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
            Person tempPerson = new Person(contactName,contactPhone);
            androidPersonList.add(tempPerson);
        }
        mainAndroidPersonList = androidPersonList;
        loadAndroidContactListView();

        androidContactListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("contactName", mainAndroidPersonList.get(position).getName());
                bundle.putString("contactNumber", mainAndroidPersonList.get(position).getPhoneNumber());
                addAndroidContactToAppFragment dialog = new addAndroidContactToAppFragment();
                dialog.setArguments(bundle);
                dialog.show(getActivity().getSupportFragmentManager(), "insertContactFragment");
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();

            }
        });
        return view;
    }
    public void loadAndroidContactListView(){

        ArrayList<String> contactListText = new ArrayList<>();

        for(int i = 0;i < mainAndroidPersonList.size(); i++ ){
            String temp = "";
            temp += mainAndroidPersonList.get(i).getName() + '\n';
            temp += mainAndroidPersonList.get(i).getPhoneNumber() + '\n';
            contactListText.add(temp);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contactListText);
        androidContactListview.setAdapter(arrayAdapter);
    }
}
