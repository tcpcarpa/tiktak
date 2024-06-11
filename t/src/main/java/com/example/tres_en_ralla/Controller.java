package com.example.tres_en_ralla;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.w3c.dom.Text;


public class Controller {
    private static Partida partida = new Partida();

    @FXML
    Button button_empezar_partida,button_abandonar_partida;

    @FXML
    TextArea vjugador1,vjugador2,djugador1,djugador2;

    @FXML
    Text tj1,tj2;

    @FXML
    Button b0,b1,b2,b3,b4,b5,b6,b7,b8,bc;

    @FXML
    Button[] listabotones = new Button[8];

    @FXML
    public void Empezar_Partida(ActionEvent event) {
        button_empezar_partida = (Button) event.getSource();
        listabotones = new Button[]{b0, b1, b2, b3, b4, b5, b6, b7, b8};

        partida.setModo(Alerts.Elige_Modo());

        if(partida.getModo() != null) {
            if(partida.getModo().equals("VSHumano")) {
                partida.EmpezarPartida();
                if(partida.getTurno()==0) {
                    MostrarTurno(tj1);
                }else {
                    MostrarTurno(tj2);
                }
                OcultarBoton(button_empezar_partida);
                MostrarBoton(button_abandonar_partida);
            }else {
                String dificultad = Alerts.Elige_Dificultad();
                if(dificultad != null) {
                    partida.setIa(dificultad);
                    partida.EmpezarPartida();
                    OcultarBoton(button_empezar_partida);
                    MostrarBoton(button_abandonar_partida);
                    if(partida.getTurno()==0) {
                        MostrarTurno(tj1);
                        if(partida.getModo().equals("ComVSCom")) {
                            TurnoCOM(partida.getCuadricula());
                        }
                    }else {
                        MostrarTurno(tj2);
                        TurnoCOM(partida.getCuadricula());
                    }
                }
            }
        }
    }

    @FXML
    public void Abandonar_Partida(ActionEvent event) {
        button_abandonar_partida = (Button) event.getSource();
        Boolean respuesta = Alerts.Abandonar_Partida();
        if(respuesta) {
            Partida.AbandonarPartida();
            Restart();
        }
    }
    @FXML
    public void Marcar(ActionEvent event) throws InterruptedException {
        if(partida.getEstado()) {
            bc = (Button) event.getSource();
            String sid = bc.getId().replaceAll("[b]","");
            int id =Integer.valueOf(sid);
            char[] cuadricula = partida.getCuadricula();
            partida.PropiedadesTurno();

            if(partida.EstadoCuadricula(cuadricula[id])) {
                bc.styleProperty().setValue("-fx-text-fill: "+partida.getColor()+";");
                bc.setText(partida.getValue());
                partida.setPosCuadricula(id,partida.getValue().charAt(0));

                if(partida.ComprobarVictoria(partida.getValue().charAt(0))) {
                    Partida.AnunciarGanador(partida.getValue().charAt(0));
                    Restart();
                    partida.FinalizarPartida();
                }else {
                    if(partida.CuadriculaLLena()) {
                        Alerts.Anunciar_Empate();
                        Restart();
                        partida.FinalizarPartida();
                    }
                    else {
                        if(partida.getTurno() == 0) {
                            SetTurno(1,tj1,tj2);
                        }else {
                            SetTurno(0,tj2,tj1);
                        }
                    }
                }
                if(!partida.getModo().equals("VSHumano") && partida.getEstado()) {
                    TurnoCOM(cuadricula);
                }
            }
        }else {
            Alerts.Debes_Empezar_Partida();
        }
    }

    private void TurnoCOM(char[] cuadricula) {
        partida.PropiedadesTurno();
        if(partida.getIa().equals("Fácil")) {
            IAFacil(cuadricula);
        }else if(partida.getIa().equals("Normal")) {
            IAFacil(cuadricula);
        }else {
            IAFacil(cuadricula);
        }

        if(partida.getEstado()) {
            if(partida.getModo().equals("ComVSCom")) {
                if(partida.getTurno()==0) {
                    SetTurno(1,tj1,tj2);
                }else {
                    SetTurno(0,tj2,tj1);
                }
                TurnoCOM(cuadricula);
            }else {
                SetTurno(0,tj2,tj1);
            }


        }
    }

    private void IAFacil(char[] cuadricula) {
        int random;
        do{
            random = (int) (Math.random()*10-1);
        }while (!partida.EstadoCuadricula(cuadricula[random]));
        listabotones[random].styleProperty().setValue("-fx-text-fill: "+partida.getColor()+";");
        listabotones[random].setText(partida.getValue());
        partida.setPosCuadricula(random,partida.getValue().charAt(0));
        if(partida.ComprobarVictoria(partida.getValue().charAt(0))) {
            Partida.AnunciarGanador(partida.getValue().charAt(0));
            Restart();
            partida.FinalizarPartida();
        }else {
            if(partida.CuadriculaLLena()) {
                Alerts.Anunciar_Empate();
                Restart();
                partida.FinalizarPartida();
            }
        }
    }

    private void OcultarBoton(Button button) {
        button.styleProperty().setValue("Visibility: false");
    }

    private void MostrarBoton(Button button) {
        button.styleProperty().setValue("Visibility: true");
    }

    private void MostrarTurno(Text tj) {
        tj.setNodeValue("Visibility:true");
    }

    private void OcultarTurno(Text tj) {tj.setNodeValue("Visibility: false"); }

    private void RefrescarMarcador(Partida partida,TextArea vj1,TextArea vj2,TextArea dj1,TextArea dj2) {
        vj1.setText(String.valueOf(partida.getVjugador1()));
        vj2.setText(String.valueOf(partida.getVjugador2()));
        dj1.setText(String.valueOf(partida.getDjugador1()));
        dj2.setText(String.valueOf(partida.getDjugador2()));
    }

    private void VaciarCuadriculaFX() {
        for (Button button : listabotones) {
            button.textProperty().setValue("");
        }
    }

    private void Restart() {
        RefrescarMarcador(partida,vjugador1,vjugador2,djugador1,djugador2);
        OcultarTurno(tj1);
        OcultarTurno(tj2);
        OcultarBoton(button_abandonar_partida);
        MostrarBoton(button_empezar_partida);
        VaciarCuadriculaFX();
    }

    private void SetTurno(int turno, Text turnoOcultar, Text turnoMostrar) {
        partida.setTurno(turno);
        OcultarTurno(turnoOcultar);
        MostrarTurno(turnoMostrar);
    }
}
