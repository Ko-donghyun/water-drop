package com.waterdrop.k.waterdrop;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Bin on 2017-09-01.
 */

public class BoardActivity extends AppCompatActivity {
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.facebook) {
            Intent i1 = new Intent(Intent.ACTION_VIEW);
            Uri u1 = Uri.parse("https://www.facebook.com");
            i1.setData(u1);
            startActivity(i1);

        } else if (id == R.id.twitter) {
            Intent i2 = new Intent(Intent.ACTION_VIEW);
            Uri u2 = Uri.parse("https://mobile.twitter.com");
            i2.setData(u2);
            startActivity(i2);
        } else if (id == R.id.instagram) {
            Intent i3 = new Intent(Intent.ACTION_VIEW);
            Uri u3 = Uri.parse("https://www.instagram.com");
            i3.setData(u3);
            startActivity(i3);
        } else if (id == R.id.school) {
            Intent i3 = new Intent(Intent.ACTION_VIEW);
            Uri u3 = Uri.parse("https://mw.pusan.ac.kr/01_main/main.html");
            i3.setData(u3);
            startActivity(i3);
        } else if (id == R.id.onestop) {
            Intent i3 = new Intent(Intent.ACTION_VIEW);
            Uri u3 = Uri.parse("https://e-onestop.pusan.ac.kr/index?home=home");
            i3.setData(u3);
            startActivity(i3);
        } else if (id == R.id.plms) {
            Intent i3 = new Intent(Intent.ACTION_VIEW);
            Uri u3 = Uri.parse("https://plms.pusan.ac.kr/login.php");
            i3.setData(u3);
            startActivity(i3);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
