package com.example.iormatrix;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MyMatrix {

    private static final long serialVersionUID = 1L;

    private final int marge = 50 ;		// in pixels
    private final int boxSize = 60 ;		// in pixels
    private final int n = 8 ;
    private int coord_X = (n/2) ;
    private int coord_Y = (n/2) ;

    private View parentview;

    public MyMatrix(View view) {
        parentview = view;
        refresh_pos();
    }

    public View getParentview(){
        return this.parentview;
    }

    //======================================================================================================

    public boolean terminal_exec(String cmd, Activity activity){
        switch (cmd.toLowerCase()){
            case "d" :
                shift_right();break;

            case "g" :
                shift_left();break;

            case "h" :
                shift_up();break;

            case "b" :
                shift_down();break;

            case "afficher":
                display(); break;

            case "cacher":
                hide(); break;

            case "init":
                restart(); break;

            case "exit":
                activity.finish(); break;

            default:
                Toast.makeText(this.parentview.getContext(), "Commande inconnue !", Toast.LENGTH_LONG).show();
                return true;

        }
        return false;

    }

    //======================================================================================================

    public void shift_right(){
        coord_X = (coord_X+1)%n;
        refresh_pos();
    }

    public void shift_left(){
        coord_X = ( (coord_X>0)? (coord_X-1) : (n-1) );
        refresh_pos();
    }

    public void shift_down(){
        coord_Y = (coord_Y+1)%n;
        refresh_pos();
    }

    public void shift_up(){
        coord_Y = ( (coord_Y>0)? (coord_Y-1) : (n-1) );
        refresh_pos();
    }

    public void display(){
        FrameLayout enabled_cell = (FrameLayout) parentview.findViewById(R.id.current_cell);
        enabled_cell.setVisibility(View.VISIBLE);
    }

    public void hide(){
        FrameLayout enabled_cell = (FrameLayout) parentview.findViewById(R.id.current_cell);
        enabled_cell.setVisibility(View.INVISIBLE);
    }

    public void restart(){
        coord_X = (n/2) ;
        coord_Y = (n/2) ;
        refresh_pos();
    }

    //======================================================================================================

    private void refresh_pos(){
        FrameLayout enabled_cell = (FrameLayout) parentview.findViewById(R.id.current_cell);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) enabled_cell.getLayoutParams();
        p.leftMargin = this.coord_X*boxSize;
        p.topMargin = this.coord_Y*boxSize;
        enabled_cell.setLayoutParams(p);
    }

    //======================================================================================================

}