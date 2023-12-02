package co.edu.ue.variosrecursos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class clFile {
    public static final int REQUEST_CODE = 23;
    private Context context;
    private Activity activity;

    public clFile(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }
    private boolean statusPermissionExternalStorage(){
        int response = ContextCompat.checkSelfPermission(this.context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (response == PackageManager.PERMISSION_GRANTED) return true;
        return false;
    }
    private void requestPermissionExternalStorage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            Toast.makeText(context, "Gestion de respuesta", Toast.LENGTH_SHORT).show();

        }

    }
    private void createDir(File file){
        if (!file.exists()){
            file.mkdirs();
        }
    }
    private void saveFile() {
        if (statusPermissionExternalStorage()) {
            // Si se tienen los permisos, proceder con la lógica de guardado de archivo
            File directory = new File(context.getExternalFilesDir(null), "NombreDeTuDirectorio"); // Cambia "NombreDeTuDirectorio" al nombre que desees
            createDir(directory);

            // Aquí debes agregar la lógica para crear y guardar tu archivo
            // Por ejemplo, puedes usar FileOutputStream y escribir datos en el archivo.

            File file = new File(directory, "nombre_del_archivo.txt"); // Cambia "nombre_del_archivo.txt" al nombre que desees para tu archivo
            try {
                // Aquí abres un FileOutputStream para escribir datos en el archivo
                // Puedes cambiarlo según tus necesidades
                FileOutputStream fos = new FileOutputStream(file);

                // Ejemplo de escritura en el archivo
                String contenido = "Hola, este es el contenido de mi archivo.";
                fos.write(contenido.getBytes());

                // Cierras el FileOutputStream después de escribir
                fos.close();

                Toast.makeText(context, "Archivo guardado con éxito", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al guardar el archivo", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Si no se tienen los permisos, solicitarlos al usuario
            requestPermissionExternalStorage();
        }
    }

}
