package i2icell.i2ibenimle.activity;


/**
 * Created by elif on 26-Jul-17.
 */

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.List;
import i2icell.i2ibenimle.R;
import i2icell.i2ibenimle.model.Campaign;
import i2icell.i2ibenimle.model.Customer;
import i2icell.i2ibenimle.model.Rateplan;
import i2icell.i2ibenimle.utils.MyLogger;
import i2icell.i2ibenimle.utils.WebService;

public class MenuActivity extends AppCompatActivity
{
    Button bRemainTl, bRemainDk, bRemainSMS, bRemainInt, bRateplan, bCampaign, bExit;
    TextView textView;
    ProgressBar progressBarOut, progressBarIn;
    TextView progressTextView, detailTextView;
    static String id = null;
    String phoneNum = null;
    WebService dataRetriever;

    private void progress(ProgressBar pb, int to)
    {
        pb.setVisibility(View.VISIBLE);
        progressTextView.setVisibility(View.VISIBLE);
        detailTextView.setVisibility(View.VISIBLE);

        ObjectAnimator animation = ObjectAnimator.ofInt (pb, "progress", 0, to); // see this max value coming back here, we animale towards that value
        animation.setDuration (2000); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();
    }

    private void setListeners()
    {
        bRemainDk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            textView.setVisibility(View.INVISIBLE);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 1);

            MyLogger.writeToLog("Kalan dakika butonuna basıld.");
            double kalanDk = dataRetriever.getBalance().getRemainVoice();      // web servisten gelecek
            double tarifedekiDk = dataRetriever.getRateplan().getVoiceAmount(); // web servisten gelecek
            int res = 100 - (int) (100 * kalanDk / tarifedekiDk);

            progress(progressBarOut, res);
            progress(progressBarIn, 100);
            progressTextView.setText(res + "%");
            detailTextView.setText("Kalan dakikalarınız: " + kalanDk + "\n\n"
                                    + dataRetriever.getBalance().getExpirationDate() + " tarihine kadar geçerlidir!");
            }
        });

        bRemainInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            textView.setVisibility(View.INVISIBLE);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 1);
            MyLogger.writeToLog("Kalan internet butonuna basıldı.");

            double kalanInt = dataRetriever.getBalance().getRemainData();      // web servisten gelecek
            double tarifedekiInt = dataRetriever.getRateplan().getDataAmount(); // web servisten gelecek
            int res = 100 - (int) (100 * kalanInt / tarifedekiInt);

            progress(progressBarOut, res);
            progress(progressBarIn, 100);
            progressTextView.setText(res + "%");
            detailTextView.setText("\nKalan internetiniz: " + kalanInt + " MB.\n\n"
                                    + dataRetriever.getBalance().getExpirationDate() + " tarihine kadar geçerlidir!");
            }
        });

        bRemainSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            textView.setVisibility(View.INVISIBLE);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 1);
                MyLogger.writeToLog("Kalan SMS butonuna basıldı.");
            double kalanSms = dataRetriever.getBalance().getRemainSMS();      // web servisten gelecek
            double tarifedekiSms = dataRetriever.getRateplan().getSMSAmount(); // web servisten gelecek
            int res = 100 - (int) (100 * kalanSms / tarifedekiSms);

            progress(progressBarOut, res);
            progress(progressBarIn, 100);
            progressTextView.setText(res + "%");
            detailTextView.setText("\nKalan SMS'leriniz: " + ((int) kalanSms) + "\n\n"
                                    + dataRetriever.getBalance().getExpirationDate() + " tarihine kadar geçerlidir!");
        }
    });

        // bitti
        bRateplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            makeInvisible();
                MyLogger.writeToLog("Tarife butonuna basıldı.");
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                StringBuilder sb = new StringBuilder();
                List<Rateplan> rateplans = dataRetriever.getRateplans();
                for (Rateplan r : rateplans) {
                    sb.append("İsim: ");
                    sb.append(r.getName()).append("\n\n");
                    sb.append("Açıklama: ");
                    sb.append(r.getDescription()).append("\n\n");
                    sb.append(r.getDataAmount()).append(" MB\n");
                    sb.append(r.getSMSAmount()).append(" SMS\n");
                    sb.append(r.getVoiceAmount()).append(" DK\n");
                    sb.append(r.getPrice()).append("TL\n---------------------------------\n\n");
                }
            textView.setText(sb.toString());
            textView.setVisibility(View.VISIBLE);
            }
        });

        bCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            makeInvisible();
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                MyLogger.writeToLog("Kampanya butonuna basıldı.");
                StringBuilder sb = new StringBuilder();
                List<Campaign> campaigns = dataRetriever.getCampaigns();
                for (Campaign r : campaigns) {
                    sb.append("İsim: ");
                    sb.append(r.getName()).append("\n\n");
                    sb.append("Açıklama: ");
                    sb.append(r.getDescription()).append("\n\n");
                    sb.append("Kurallar: ");
                    sb.append(r.getRules()).append("\n---------------------------------\n\n");
                }
                textView.setText(sb.toString());
                textView.setVisibility(View.VISIBLE);
            }
        });

        bRemainTl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            makeInvisible();
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                MyLogger.writeToLog("Kalan TL butonuna basıldı.");

                double kalanTL = dataRetriever.getWallet().getAmount(); // servisten gelecek.
            textView.setText("\n\n Kalan TL: " + kalanTL);
            textView.setVisibility(View.VISIBLE);
            }
        });


        bExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLogger.writeToLog("Çıkış butonuna basıldı.");
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
                Toast.makeText(getApplicationContext(), "Çıkış başarıyla yapıldı!" , Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void makeInvisible()
    {
        progressBarOut.setVisibility(View.INVISIBLE);
        progressBarIn.setVisibility(View.INVISIBLE);
        progressTextView.setVisibility(View.INVISIBLE);
        detailTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            id = extras.getString("userId");
            phoneNum = extras.getString("userPhone");

            try
            {
                dataRetriever = new WebService(id);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        HomePageRetriever task = new HomePageRetriever();
        task.execute(id);

        bRemainTl = (Button) findViewById(R.id.butttonKalanTl);
        bRemainDk = (Button) findViewById(R.id.butttonKalanDk);
        bRemainSMS = (Button) findViewById(R.id.butttonKalanSMS);
        bRemainInt = (Button) findViewById(R.id.butttonKalanİnt);
        bRateplan = (Button) findViewById(R.id.butttonTarife);
        bCampaign = (Button) findViewById(R.id.butttonKampanya);
        bExit = (Button) findViewById(R.id.butttonCikis);
        textView = (TextView) findViewById(R.id.infoText);
        textView.setMovementMethod(new ScrollingMovementMethod());
        detailTextView = (TextView) findViewById(R.id.textViewDetail);
        detailTextView.setVisibility(View.INVISIBLE);

        progressBarOut = (ProgressBar) findViewById(R.id.progressBar);
        progressBarIn = (ProgressBar) findViewById(R.id.progressBar2);
        progressBarOut.setVisibility(View.INVISIBLE);
        progressBarIn.setVisibility(View.INVISIBLE);

        progressBarIn.setAlpha(0.5f);
        progressBarOut.setAlpha(0.9f);

        progressTextView = (TextView) findViewById(R.id.progressBarText);
        progressTextView.setVisibility(View.INVISIBLE);

        progressTextView.setTextColor(Color.parseColor("#34AADC"));
        progressTextView.setTypeface(null, Typeface.BOLD);
        progressTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        detailTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        setListeners();
    }

    public class HomePageRetriever extends AsyncTask<String, String, String> //Ana giriş ekranı sayın olan
    {
        @Override
        protected String doInBackground(String... params)
        {
            String METHOD_NAME = "getCustomerCredential";
            String SOAP_ACTION = WebService.NAMESPACE + METHOD_NAME;
            SoapObject request = new SoapObject(WebService.NAMESPACE, METHOD_NAME);
            request.addProperty("arg0", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(WebService.URL);

            try
            {
                androidHttpTransport.call(SOAP_ACTION, envelope);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            SoapObject soapObjectReturn =(SoapObject) soapObject.getProperty("return");

            Customer customer = new Customer();
            customer.setFirstName(soapObjectReturn.getProperty("first_name").toString());
            customer.setLastName(soapObjectReturn.getProperty("last_name").toString());
            return customer.getFullName();
        }

        @Override
        protected void onPostExecute(String result)
        {
            textView.setText(phoneNum + "\n\n\n" +
                    "  Hoşgeldiniz ! \n" + result + "\n\n" +
                    "  Tarifeniz\n " + dataRetriever.getRateplan().getName());
        }
    }
}


