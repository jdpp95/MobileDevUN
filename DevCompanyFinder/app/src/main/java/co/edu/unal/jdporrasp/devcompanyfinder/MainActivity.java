package co.edu.unal.jdporrasp.devcompanyfinder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.edu.unal.jdporrasp.devcompanyfinder.DB.CompanyOperations;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button addCompanyButton;
    private Button editCompanyButton;
    private Button deleteCompanyButton;
    private Button viewAllCompaniesButton;
    private CompanyOperations companyOps;
    private static final String EXTRA_COMPANY_ID = "co.edu.unal.jdporrasp.companyId";
    private static final String EXTRA_ADD_UPDATE = "co.edu.unal.jdporrasp.add_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addCompanyButton = findViewById(R.id.button_add_company);
        editCompanyButton = findViewById(R.id.button_edit_company);
        deleteCompanyButton = findViewById(R.id.button_delete_company);
        viewAllCompaniesButton = findViewById(R.id.button_view_companies);
        companyOps = new CompanyOperations(this);

        addCompanyButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, AddUpdateCompany.class);
                        i.putExtra(EXTRA_ADD_UPDATE, "Add");
                        startActivity(i);
                    }
                }
        );

        editCompanyButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getCompanyIdAndUpdateCompany();
                    }
                }
        );

        deleteCompanyButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getCompanyIdAndRemoveCompany();
                    }
                }
        );

        viewAllCompaniesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ViewAllCompanies.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.company_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.menu_item_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCompanyIdAndUpdateCompany(){
        LayoutInflater li = LayoutInflater.from(this);
        View getCompanyIdView = li.inflate(R.layout.dialog_get_company_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(getCompanyIdView);

        final EditText userInput = (EditText) getCompanyIdView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, AddUpdateCompany.class);
                        intent.putExtra(EXTRA_ADD_UPDATE, "Update");
                        intent.putExtra(EXTRA_COMPANY_ID, Long.parseLong(userInput.getText().toString()));
                        startActivity(intent);
                    }
                }).create()
                .show();
    }

    public void getCompanyIdAndRemoveCompany(){
        LayoutInflater li = LayoutInflater.from(this);
        View getCompanyIdView = li.inflate(R.layout.dialog_get_company_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(getCompanyIdView);

        final EditText userInput = (EditText) getCompanyIdView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        companyOps = new CompanyOperations(MainActivity.this);
                        companyOps.open();
                        companyOps.removeCompany(companyOps.getCompany(Long.parseLong(userInput.getText().toString())));
                        companyOps.close();
                        Toast.makeText(MainActivity.this, "Company removed successfully!", Toast.LENGTH_SHORT).show();;
                    }
                }).create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        companyOps.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        companyOps.close();
    }
}
