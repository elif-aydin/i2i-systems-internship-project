package i2icell.i2ibenimle.utils;

import android.os.AsyncTask;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import i2icell.i2ibenimle.model.Balance;
import i2icell.i2ibenimle.model.Campaign;
import i2icell.i2ibenimle.model.Rateplan;
import i2icell.i2ibenimle.model.Wallet;

/**
 * Created by elif on 24-Jul-17.
 */

public class WebService
{
    public static String NAMESPACE = "http://i2i_benimle/";
    public static String URL = "http://172.30.10.7:9090/i2i_benimle?wsdl";
    private Balance balance;
    private Wallet wallet;
    private List<Campaign> campaigns;
    private Rateplan rateplan;
    private List<Rateplan> rateplans;

    public WebService(String id) throws ExecutionException, InterruptedException
    {
        balance = getBalance(id);
        wallet = getWallet(id);
        campaigns = getCampaigns(id);
        rateplan = getRatePlan(id);
        rateplans = getRateplans(id);
    }

    public static List<Campaign> getCampaigns(String id) throws ExecutionException, InterruptedException
    {
        CampaignRetriever cr = new CampaignRetriever();
        List<Campaign> res = cr.execute(id).get();

        return res;
    }

    public static Rateplan getRatePlan(String id) throws ExecutionException, InterruptedException
    {
        RateplanRetriever rt = new RateplanRetriever();
        Rateplan res = rt.execute(id).get();

        return res;
    }

    public static Balance getBalance(String id) throws ExecutionException, InterruptedException
    {
        BalanceRetriever br = new BalanceRetriever();
        Balance res = br.execute(id).get();

        return res;
    }

    public static Wallet getWallet(String id) throws ExecutionException, InterruptedException
    {
        WalletRetriever wt = new WalletRetriever();
        Wallet res = wt.execute(id).get();

        return res;
    }

    public static List<Rateplan> getRateplans(String id) throws ExecutionException, InterruptedException
    {
        AllRateplansRetriever rt = new AllRateplansRetriever();
        List<Rateplan> res = rt.execute(id).get();

        return res;
    }

    public static class CampaignRetriever extends AsyncTask<String, String, List<Campaign>>
    {
        String METHOD_NAME = "getCampaign";
        String SOAP_ACTION = WebService.NAMESPACE + METHOD_NAME;
        List<Campaign> campaigns = null;

        @Override
        protected List<Campaign> doInBackground(String... params)
        {
            SoapObject request = new SoapObject(WebService.NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(WebService.URL);
            List<Campaign> campaigns = new ArrayList<>();

            try
            {
                androidHttpTransport.call(SOAP_ACTION, envelope);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            SoapObject soapObject = (SoapObject) envelope.bodyIn;

            int count = soapObject.getPropertyCount();
            for (int i = 0; i < count; i++)
            {
                Object obj = soapObject.getProperty(i);
                if (obj instanceof SoapObject)
                {
                    SoapObject sobj = (SoapObject) obj;
                    Campaign campaign = new Campaign();
                    campaign.setDescription(sobj.getProperty("description").toString());
                    campaign.setName(sobj.getProperty("name").toString());
                    campaign.setRules(sobj.getProperty("rules").toString());
                    campaigns.add(campaign);
                }
            }
            return campaigns;
        }
        @Override
        protected void onPostExecute(List<Campaign> result)
        {
            campaigns = result;
        }
    }

    public static class RateplanRetriever extends AsyncTask<String, String, Rateplan>
    {
        String METHOD_NAME = "getRateplan";
        String SOAP_ACTION = WebService.NAMESPACE + METHOD_NAME;
        Rateplan rateplan = null;

        @Override
        protected Rateplan doInBackground(String... params)
        {
            SoapObject request = new SoapObject(WebService.NAMESPACE, METHOD_NAME);
            request.addProperty("arg0", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(WebService.URL);
            Rateplan rateplan = new Rateplan();

            try
            {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                SoapObject soapObjectReturn =(SoapObject) soapObject.getProperty("return");
                rateplan.setDataAmount(Integer.parseInt(soapObjectReturn.getProperty("data_amount").toString()));
                rateplan.setDescription(soapObjectReturn.getProperty("description").toString());
                rateplan.setName(soapObjectReturn.getProperty("name").toString());
                rateplan.setSMSAmount(Integer.parseInt(soapObjectReturn.getProperty("sms_amount").toString()));
                rateplan.setVoiceAmount(Integer.parseInt(soapObjectReturn.getProperty("voice_amount").toString()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return rateplan;
        }

        @Override
        protected void onPostExecute(Rateplan result)
        {
            rateplan = result;
        }
    }

    public static class BalanceRetriever extends AsyncTask<String, String, Balance>
    {
        String METHOD_NAME = "getBalance";
        String SOAP_ACTION = WebService.NAMESPACE + METHOD_NAME;
        Balance balance = null;

        @Override
        protected Balance doInBackground(String... params)
        {
            SoapObject request = new SoapObject(WebService.NAMESPACE, METHOD_NAME);
            request.addProperty("arg0", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(WebService.URL);
            Balance balance = new Balance();

            try
            {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                SoapObject soapObjectReturn =(SoapObject) soapObject.getProperty("return");
                balance.setRemainData(Integer.parseInt(soapObjectReturn.getProperty("remaining_data").toString()));
                balance.setRemainSMS(Integer.parseInt(soapObjectReturn.getProperty("remaining_sms").toString()));
                balance.setRemainVoice(Integer.parseInt(soapObjectReturn.getProperty("remaining_voice").toString()));
                balance.setExpirationDate((soapObjectReturn.getProperty("expiration_date").toString()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return balance;
        }

        @Override
        protected void onPostExecute(Balance result)
        {
            balance = result;
        }
    }

    public static class WalletRetriever extends AsyncTask<String, String, Wallet>
    {
        String METHOD_NAME = "getCustomerWallet";
        String SOAP_ACTION = WebService.NAMESPACE + METHOD_NAME;
        Wallet wallet = null;

        @Override
        protected Wallet doInBackground(String... params)
        {
            SoapObject request = new SoapObject(WebService.NAMESPACE, METHOD_NAME);
            request.addProperty("arg0", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(WebService.URL);
            Wallet wallet = new Wallet();

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
            wallet.setAmount(Integer.parseInt(soapObjectReturn.getProperty("amount").toString()));

            return wallet;
        }

        @Override
        protected void onPostExecute(Wallet result)
        {
            wallet = result;
        }
    }

    public static class AllRateplansRetriever extends AsyncTask<String, String, List<Rateplan>>
    {
        String METHOD_NAME = "getRateplanList";
        String SOAP_ACTION = WebService.NAMESPACE + METHOD_NAME;
        List<Rateplan> rateplans = null;

        @Override
        protected List<Rateplan> doInBackground(String... params)
        {
            SoapObject request = new SoapObject(WebService.NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(WebService.URL);
            List<Rateplan> rateplans = new ArrayList<>();

            try
            {
                androidHttpTransport.call(SOAP_ACTION, envelope);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            SoapObject soapObject = (SoapObject) envelope.bodyIn;

            int count = soapObject.getPropertyCount();
            for (int i = 0; i < count; i++)
            {
                Object obj = soapObject.getProperty(i);
                if (obj instanceof SoapObject)
                {
                    SoapObject sobj = (SoapObject) obj;
                    Rateplan rateplan = new Rateplan();
                    rateplan.setDescription(sobj.getProperty("description").toString());
                    rateplan.setName(sobj.getProperty("name").toString());
                    rateplan.setVoiceAmount(Integer.parseInt(sobj.getProperty("voice_amount").toString()));
                    rateplan.setSMSAmount(Integer.parseInt(sobj.getProperty("sms_amount").toString()));
                    rateplan.setDataAmount(Integer.parseInt(sobj.getProperty("data_amount").toString()));
                    rateplan.setPrice(Integer.parseInt(sobj.getProperty("price").toString()));
                    rateplans.add(rateplan);
                }
            }
            return rateplans;
        }
        @Override
        protected void onPostExecute(List<Rateplan> result)
        {
            rateplans = result;
        }
    }

    public Balance getBalance()
    {
        return balance;
    }

    public Wallet getWallet()
    {
        return wallet;
    }

    public List<Campaign> getCampaigns()
    {
        return campaigns;
    }

    public Rateplan getRateplan()
    {
        return rateplan;
    }

    public List<Rateplan> getRateplans()
    {
        return rateplans;
    }

}