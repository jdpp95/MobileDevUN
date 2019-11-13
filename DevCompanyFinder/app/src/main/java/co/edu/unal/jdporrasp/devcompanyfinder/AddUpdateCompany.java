package co.edu.unal.jdporrasp.devcompanyfinder;

import androidx.appcompat.app.AppCompatActivity;
import co.edu.unal.jdporrasp.devcompanyfinder.DB.CompanyOperations;
import co.edu.unal.jdporrasp.devcompanyfinder.model.Company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Date;

public class AddUpdateCompany extends AppCompatActivity {

    private static final String EXTRA_COMPANY_ID = "co.edu.unal.jdporrasp.companyId";
    private static final String EXTRA_ADD_UPDATE = "co.edu.unal.jdporrasp.add_update";
    //private static final String DIALOG_DATE = "DialogDate";
    private RadioGroup radioGroup;
    private RadioButton consultingRadioButton, customDevRadioButton, softwareFactoryButton;
    private EditText nameEditText;
    private EditText webSiteEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText productsServicesEditText;
    private Button addUpdateButton;
    private Company oldCompany;
    private Company newCompany;
    private String mode;
    private long companyId;
    private CompanyOperations companyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_company);
        oldCompany = new Company();
        newCompany = new Company();
        nameEditText = findViewById(R.id.edit_text_name);
        webSiteEditText = findViewById(R.id.edit_text_website);
        emailEditText = findViewById(R.id.edit_text_email);
        phoneEditText = findViewById(R.id.edit_text_phone);
        productsServicesEditText = findViewById(R.id.edit_text_products_services);
        radioGroup = findViewById(R.id.radio_category);
        consultingRadioButton = findViewById(R.id.radio_consulting);
        customDevRadioButton = findViewById(R.id.radio_custom_dev);
        softwareFactoryButton = findViewById(R.id.radio_software_factory);
        addUpdateButton = findViewById(R.id.button_add_update_company);
        companyData = new CompanyOperations(this);
        companyData.open();

        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);
        if(mode.equals("Update")){
            addUpdateButton.setText(R.string.update_company);
            companyId = getIntent().getLongExtra(EXTRA_COMPANY_ID, 0);

            initializeCompany(companyId);
        }

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        if (checkedId == R.id.radio_consulting) {
                            newCompany.setCategory(getResources().getString(R.string.string_consulting));
                            if (mode.equals("Update")) {
                                oldCompany.setCategory(getResources().getString(R.string.string_consulting));
                            }
                        } else if (checkedId == R.id.radio_custom_dev) {
                            newCompany.setCategory(getResources().getString(R.string.string_custom_dev));
                            if (mode.equals("Update")) {
                                oldCompany.setCategory(getResources().getString(R.string.string_custom_dev));
                            }
                        } else if (checkedId == R.id.radio_software_factory) {
                            newCompany.setCategory(getResources().getString(R.string.string_software_factory));
                            if (mode.equals("Update")) {
                                oldCompany.setCategory(getResources().getString(R.string.string_software_factory));
                            }
                        }
                    }
                }
        );

        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode.equals("Add")){
                    newCompany.setName(nameEditText.getText().toString());
                    newCompany.setEmail(emailEditText.getText().toString());
                    newCompany.setPhoneNumber(phoneEditText.getText().toString());
                    newCompany.setProductsAndServices(productsServicesEditText.getText().toString());
                    newCompany.setWebsite(webSiteEditText.getText().toString());
                    companyData.addCompany(newCompany);

                    Toast.makeText(AddUpdateCompany.this, "Company " + newCompany.getName() + " has been added successfully!", Toast.LENGTH_SHORT).show();
                } else if (mode.equals("Update")) {
                    oldCompany.setName(nameEditText.getText().toString());
                    oldCompany.setEmail(emailEditText.getText().toString());
                    oldCompany.setPhoneNumber(phoneEditText.getText().toString());
                    oldCompany.setProductsAndServices(productsServicesEditText.getText().toString());
                    oldCompany.setWebsite(webSiteEditText.getText().toString());
                    companyData.updateCompany(oldCompany);

                    Toast.makeText(AddUpdateCompany.this, "Company " + newCompany.getName() + " has been updated successfully!", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(AddUpdateCompany.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeCompany(long companyId) {
        oldCompany = companyData.getCompany(companyId);
        nameEditText.setText(oldCompany.getName());
        emailEditText.setText(oldCompany.getEmail());
        phoneEditText.setText(oldCompany.getPhoneNumber());
        webSiteEditText.setText(oldCompany.getWebsite());
        productsServicesEditText.setText(oldCompany.getProductsAndServices());
        String category = oldCompany.getCategory();
        int checkId = -1;

        if(category.equals(getResources().getString(R.string.string_software_factory)))
            checkId = R.id.radio_software_factory;
        else if (category.equals(getResources().getString(R.string.string_custom_dev)))
            checkId = R.id.radio_custom_dev;
        else if (category.equals(getResources().getString(R.string.string_consulting)))
            checkId = R.id.radio_consulting;

        radioGroup.check(checkId);
    }

}
