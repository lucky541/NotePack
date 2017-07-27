package ltd.pvt.lucky.notepack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
  static   EditText text;
    String fileNameHolder[];
    ArrayAdapter <String> arrayAdapter;
    String fileName,fileData;  // fileName = tableName
    DataBaseHelper mydb;
    Button saveButton,readButton,newButton,deleteButton,showFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=(EditText)findViewById(R.id.editText);
        saveButton=(Button) findViewById(R.id.button_save);
        readButton=(Button) findViewById(R.id.button_read);
        newButton=(Button) findViewById(R.id.button_new);
        deleteButton=(Button) findViewById(R.id.button_delete);
        showFiles=(Button) findViewById(R.id.button_files);
        mydb = new DataBaseHelper(MainActivity.this);
        saveData();
        readData();
        newData();
        deleteData();
        showFiles();
    }

//  this will create the table and save the data into that
    private void saveData() {
       saveButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               final EditText input = new EditText(MainActivity.this);
               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setCancelable(false);
               builder.setTitle("Save File..!");
               builder.setMessage("Enter File Name ");
               input.setHint("File Name");
               builder.setView(input);
               builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       fileName = input.getText().toString();
                       fileData = text.getText().toString();

                       long status = mydb.deleteFile(fileName);
                       /*  if(status>0)
                           Toast.makeText(MainActivity.this,fileName+" is deleted.",Toast.LENGTH_SHORT).show();
                       else
                           Toast.makeText(MainActivity.this,fileName+" can not be deleted.",Toast.LENGTH_SHORT).show();
                          */

                        status =  mydb.saveFile(fileName,fileData);
                       if(status>-1) {
                           Toast.makeText(MainActivity.this, " File Saved", Toast.LENGTH_SHORT).show();

                       }else
                           Toast.makeText(MainActivity.this," Oops error File can not be saved",Toast.LENGTH_SHORT).show();


                   }
               });

               builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               });
               builder.create();
               builder.show();
           }

       });
    }

    private void readData() {


        readButton.setOnClickListener(new View.OnClickListener() {

             StringBuffer stringBuffer;
            @Override
            public void onClick(View v) {
                text.setText("");
                final AutoCompleteTextView input = new AutoCompleteTextView(MainActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Open File..!");
                builder.setMessage("Enter File Name ");
                input.setHint("File Name");

                //set fileNameHolder for the Auto Complete
                Cursor cursor = mydb.getAllFileNames();
                fileNameHolder = new String[cursor.getCount()];
                int index=0;
                while (cursor.moveToNext()){
                    fileNameHolder[index]=cursor.getString(0)+"\n";
                    index++;
                }

                arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.select_dialog_item,fileNameHolder);
                input.setAdapter(arrayAdapter);
                input.setThreshold(1);

                builder.setView(input);
                builder.setPositiveButton("open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileName = input.getText().toString().replace("\n","");
                        Cursor status =  mydb.readFile(fileName);
                        stringBuffer = new StringBuffer("");
                        while (status.moveToNext()){
                          stringBuffer.append(status.getString(1));
                          }
                        text.setText(stringBuffer.toString());
                        }
                    });


                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();

            }
        });

    }

    private void newData() {
     newButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             text.setText("");
         }
     });


    }

    private void deleteData() {

        deleteButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text.setText("");
                final AutoCompleteTextView input = new AutoCompleteTextView(MainActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Open File..!");
                builder.setMessage("Enter File Name ");
                input.setHint("File Name");

                //set fileNameHolder for the Auto Complete
                Cursor cursor = mydb.getAllFileNames();
                fileNameHolder = new String[cursor.getCount()];
                int index = 0;
                while (cursor.moveToNext()) {
                    fileNameHolder[index] = cursor.getString(0) + "\n";
                    index++;
                }

                arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item, fileNameHolder);
                input.setAdapter(arrayAdapter);
                input.setThreshold(1);
            /*
            String[] allFileNames;
            @Override
            public void onClick(View v) {
                text.setText("");
               final EditText input = new EditText(MainActivity.this);
               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Delete file");

 */
                builder.setView(input);
                builder.setPositiveButton("delete",new DialogInterface.OnClickListener() {



                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = input.getText().toString().replace("\n","");
                        int status = mydb.deleteFile(fileName);

                        if(status>0)
                            Toast.makeText(MainActivity.this,fileName+" is deleted.",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this,fileName+" can not be deleted.",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }

        });

    }

    public  String[]  showFiles(){
        showFiles.setOnClickListener( new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             text.setText("");
             ArrayAdapter arrayAdapter;
             Cursor cursor = mydb.getAllFileNames();
                   fileNameHolder = new String[cursor.getCount()];
                int index=0;
                while (cursor.moveToNext()){
                    fileNameHolder[index]=cursor.getString(0)+"\n";
                     index++;
                }
                ListView listView = new ListView(MainActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("All Files");
                //allFileNames = new String[]{"lucky","hemant"};
               arrayAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.filenames,fileNameHolder);
                listView.setAdapter(arrayAdapter);
                builder.setView(listView);
                builder.create();
                builder.show();

         }
     });
        return  fileNameHolder;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
