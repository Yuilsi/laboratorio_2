package com.example.laboratorio_2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
//variables de diseño
    private TextView preguntaText;
    private EditText respuestaUsuario;
    private TextView contadorText;
    private TextView puntajeText;
    private Button botonResponder;
//variables de funcionamiento

    private Pregunta preguntaActual;
    private int tiempoRestante;
    private int puntaje;
    private int tiempoPulsado;
    private boolean isPressing;

    //boton de reinicio
    private Button botonReiniciar;

    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preguntaText = findViewById(R.id.preguntaText);
        respuestaUsuario = findViewById(R.id.respuestaUsuario);
        contadorText = findViewById(R.id.contadorText);
        puntajeText = findViewById(R.id.puntajeText);
        botonResponder = findViewById(R.id.botonResponder);
        botonReiniciar= findViewById(R.id.botonReiniciar);

        iniciarJuego();
        generarNuevaPregunta();

// Botones

        botonResponder.setOnClickListener(
                (view) -> {
                    verificarRespuesta();

                }
        );

        botonReiniciar.setOnClickListener(
                (view)->{
                    iniciarJuego();
                }
        );

 //Acción de tocar la pantalla(texto) y cambiar la pregunta
        preguntaText.setOnTouchListener(
                (view,motionEvent)->{
              switch(motionEvent.getAction()){
                  case MotionEvent.ACTION_DOWN:
                      isPressing=true;
                      new Thread(()->{
                          tiempoPulsado=0;
                          while(tiempoPulsado<1500){
                              try{
                             Thread.sleep(1);
                                  tiempoPulsado +=150;
                              if(!isPressing) {
                                  return;
                              }
                              }catch(InterruptedException e){
                              }
                          }
                          runOnUiThread(()-> {
                             generarNuevaPregunta();
                          });

                  }).start();
                    break;
                    case MotionEvent.ACTION_UP:
                      isPressing= false;
                    break;
              }
                    return true;
                });
    }
  //métodos

        public void verificarRespuesta(){
           String respuestaTexto= respuestaUsuario.getText().toString();
           int respuestaInt=(int)Integer.parseInt(respuestaTexto);

           if(respuestaInt == preguntaActual.getRespuesta()){
               Toast.makeText(this,"correcto", Toast.LENGTH_SHORT).show();
               puntaje +=5;
               puntajeText.setText("Puntaje:"+puntaje);
           }else{
               Toast.makeText(this,"mal", Toast.LENGTH_SHORT).show();
               puntaje -=4;
               puntajeText.setText("Puntaje:"+puntaje);
               if(puntaje<=0){
                   Toast.makeText(this,"perdiste", Toast.LENGTH_SHORT).show();
                   tiempoRestante=0;
                   contadorText.setText(""+ tiempoRestante);
                   botonResponder.setEnabled(false);                                         //si el puntaje es menor o igual a cero,el tiempo automaticamente cambia a cero y aparece le boton de "intentar de nuevo"
                   botonReiniciar.setVisibility(View.VISIBLE);
               }
           }

           generarNuevaPregunta();
        }
    //--------------------------------------------------------------------------------------------------
        public void generarNuevaPregunta(){
        preguntaActual= new Pregunta();
        preguntaText.setText(preguntaActual.getPregunta());
        }
    //--------------------------------------------------------------------------------------------------
        private void pierdePorTiempo(){
        if(tiempoRestante==0){
            botonReiniciar.setVisibility(View.VISIBLE);
            botonResponder.setEnabled(false);
        } }
    //--------------------------------------------------------------------------------------------------
        private void iniciarJuego(){
        generarNuevaPregunta();
        botonResponder.setEnabled(true);
        botonReiniciar.setVisibility(View.INVISIBLE);
        puntaje=0;
        puntajeText.setText("Puntaje:"+puntaje);


        tiempoRestante=30;
        contadorText.setText(""+ tiempoRestante);

            new Thread(
                    ()-> {
                        while(tiempoRestante >0){
                            try {
                                tiempoRestante--;
                                runOnUiThread(
                                        ()->{
                                            contadorText.setText(""+ tiempoRestante);
                                            pierdePorTiempo();
                                        }
                                );

                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Log.e("error", e.toString());

                            }

                        }

                    }

            ).start();
        }
//--------------------------------------------------------------------------------------------------

    }
