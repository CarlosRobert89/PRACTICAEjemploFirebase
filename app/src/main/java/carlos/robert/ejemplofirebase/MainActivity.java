package carlos.robert.ejemplofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import carlos.robert.ejemplofirebase.databinding.ActivityMainBinding;
import carlos.robert.ejemplofirebase.modelos.Persona;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseDatabase database; //conector BD
    private DatabaseReference fraseRef; //referencias
    private DatabaseReference personaRef;
    private DatabaseReference listaPersonasRef;
    private ArrayList<Persona> listaPersonas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaPersonas = new ArrayList<>();

        //conexión con la BD (url: de la mi base de datos en Firebase)

        database = FirebaseDatabase.getInstance("https://ejemplofirebase-29414-default-rtdb.europe-west1.firebasedatabase.app/");
        //inicializar referencias
        fraseRef = database.getReference("frase"); //referencia (string)
        personaRef = database.getReference("persona"); //referencia (objeto)
        listaPersonasRef = database.getReference("lispersonas"); //referencia (ArrayList)

        binding.btnGuardarMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fraseRef.setValue(binding.txtFraseMain.getText().toString()); //escribir frase en la BD
                int edad = (int) (Math.random() * 100);
                Persona p = new Persona(binding.txtFraseMain.getText().toString(), edad);
                personaRef.setValue(p); //escribir objeto en la BD
                listaPersonas.add(p);
                listaPersonasRef.setValue(listaPersonas); //escribir lista en la BD
            }
        });

        //Traer datos de la BD (leer)
        fraseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String texto = snapshot.getValue(String.class); //texto
                //mostrar el mensaje en pantalla
                binding.lbFraseMain.setText(texto);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Traer datos de la BD (leer)
        personaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Persona p = snapshot.getValue(Persona.class); //objeto
                binding.lbFraseMain.setText(p.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Traer datos de la BD (leer)
        listaPersonasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //*Al ser una lista tenemos que hacer un gti
                GenericTypeIndicator<ArrayList<Persona>> gti = new GenericTypeIndicator<ArrayList<Persona>>() {};
                //*
                ArrayList<Persona> lista = snapshot.getValue(gti); //lista de objetos
                binding.lbFraseMain.setText("Elemento en la lista " + lista.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}