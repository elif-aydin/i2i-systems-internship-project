package i2icell.i2ibenimle.activity;

/**
 * Created by elif on 26-Jul-17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import i2icell.i2ibenimle.R;
import i2icell.i2ibenimle.utils.MyLogger;
import i2icell.i2ibenimle.utils.WebService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    Button btnLogin;
    EditText edPhoneNum, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edPhoneNum = (EditText) findViewById(R.id.editTelefonNumarasi);
        edPassword = (EditText) findViewById(R.id.editSifre);
        btnLogin = (Button) findViewById(R.id.butttonLogin);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        return true;
    }

    @Override
    public void onClick(View view)
    {
        if (edPhoneNum.getText() != null && edPassword.getText() != null)
        {
            String phoneNumber = edPhoneNum.getText().toString();
            MyLogger.writeToLog("Telefon numarası girildi : " + phoneNumber);
            String password = edPassword.getText().toString();
            MyLogger.writeToLog("Şifre girildi : " + password);


            if (!phoneNumber.isEmpty() && !password.isEmpty())
            {
                IdRetriever task = new IdRetriever();
                task.execute(phoneNumber, password);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Telefon numarası/şifre boş bırakılamaz!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class IdRetriever extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            String METHOD_NAME = "getContractId";
            String SOAP_ACTION = WebService.NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(WebService.NAMESPACE, METHOD_NAME);
            request.addProperty(" ", params[0]);
            request.addProperty("arg1", params[1]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(WebService.URL);
            String contractID = null;

            try
            {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                contractID = soapObject.getProperty(0).toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return contractID;
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(result != null && !result.equals("-1"))
            {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("userId", result);
                intent.putExtra("userPhone", edPhoneNum.getText().toString());
                MyLogger.writeToLog("Giriş başarıyla gerçekleşti. Kullanıcı id = " + result);
                startActivity(intent);
            }
            else
                if(edPhoneNum.getText().toString().length() !=  11)
                {
                    MyLogger.writeToLog("Giriş başarısız. Telefon numarası eksik girildi." + edPhoneNum.getText().toString() );
                    Toast.makeText(getApplicationContext(), "Telefon numarası 11 sayıdan oluşmalıdır!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    MyLogger.writeToLog("Giriş başarısız. Telefon numarası veya şifre yanlış girildi.");
                    Toast.makeText(getApplicationContext(), "Şifre veya telefon numarası hatalı!", Toast.LENGTH_SHORT).show();
                }
        }
    }
}