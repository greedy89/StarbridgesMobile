package id.co.indocyber.android.starbridges.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import id.co.indocyber.android.starbridges.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import id.co.indocyber.android.starbridges.model.EmployeeShiftSchedule.EmployeeShiftSchedule;
import id.co.indocyber.android.starbridges.model.getimage.GetImage;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import id.co.indocyber.android.starbridges.utility.SessionManagement;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private String username,fullname, token;
    ProgressDialog progressDialog;
    APIInterfaceRest apiInterface;
    CircleImageView imageView;
    private TextView mUsernameView;
    private String attendancePrivilege="";
    SessionManagement session;
    Button btnSignOut;
    Button AttendanceButton;
    static final int REQUEST_ACCESS_LOCATION = 101;
    private List<String> listMenu;
    private LinearLayout lytHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imageView = (CircleImageView)findViewById(R.id.profile_image);
        lytHome = (LinearLayout)findViewById(R.id.lytHome);
        session = new SessionManagement(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        String token_sp = user.get(SessionManagement.KEY_TOKEN);
        String loginName_sp = user.get(SessionManagement.KEY_LOGINNAME);
        String fullName_sp = user.get(SessionManagement.KEY_FULLNAME);
        String tokenExpiredDate=user.get(SessionManagement.KEY_EXPIRES);
        String location=user.get(SessionManagement.KEY_LOCATION);
        String locationId=user.get(SessionManagement.KEY_LOCATION_ID);
        String employeeId=user.get(SessionManagement.KEY_EMPLOYEE_ID);
        attendancePrivilege=user.get(SessionManagement.KEY_ATTENDANCE_PRIVILEGE);

                //Thu, 31 May 2018 01:34:37 GMT
        DateFormat df = new SimpleDateFormat("EEE, dd MMMM yyyy kk:mm:ss z", Locale.ENGLISH);
        Date tokenExpired;
        try{
            tokenExpired =  df.parse(tokenExpiredDate);
            Date checkDate = new Date();
            if(checkDate.after(tokenExpired))
            {
                session.logoutUser();
                Intent intent=new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e)
        {
            Log.d("error parse", "error parse");
        }

        GlobalVar.setEmployeeId(employeeId);
        GlobalVar.setToken(token_sp);
        GlobalVar.setLoginName(loginName_sp);
        GlobalVar.setFullname(fullName_sp);
        GlobalVar.setLocation(location);
        GlobalVar.setLocationId(locationId);
        GlobalVar.setAttendancePrivilege(attendancePrivilege);
        username = GlobalVar.loginName();
        fullname = GlobalVar.getFullname();
        mUsernameView=(TextView) findViewById(R.id.lbl_username);
        mUsernameView.setText("Hello,\n"+fullname);

        loadingImage();
        getEmployeeSchedule();

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            setToolbar();
        }

        listMenu= new ArrayList<>();
        listMenu.add("attendance");
        listMenu.add("outOfOffice");
        listMenu.add("attendanceHistory");
        listMenu.add("medical");
        listMenu.add("correction");
        listMenu.add("leaveRequest");
        listMenu.add("leaveCancelation");
        listMenu.add("transportReimburse");
        listMenu.add("overtimeReimburse");
        listMenu.add("signOut");

        createDynamicMenu();
    }

    private void createDynamicMenu()
    {
        int c=1;
        HashMap<Integer, LinearLayout> hshLytHome = new HashMap<>();
        if(listMenu.size()%3!=0)
        {
            for(int i=0;i<listMenu.size()%3;i++)
            {
                listMenu.add("");
            }
        }
        for(String string: listMenu)
        {

            LinearLayout lytForHome= new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            if((c-1)/3==0)
                params.setMargins(0,getResources().getDimensionPixelSize(R.dimen.spacing_30dp),0,0);
            else
                params.setMargins(0,getResources().getDimensionPixelSize(R.dimen.spacing_10dp),0,0);
            lytForHome.setLayoutParams(params);
            lytForHome.setBaselineAligned(false);

            if(c%3==1)
            {

                if(hshLytHome.get(c/3)==null)
                    hshLytHome.put(c/3, lytForHome);
            }
            if(string.equalsIgnoreCase("attendance"))
            {
                addViewToLayoutHome("ATTENDANCE", R.mipmap.ic_attendance4, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase("outOfOffice"))
            {
                addViewToLayoutHome("OUT OF OFFICE", R.mipmap.ic_out_of_office4, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase("attendanceHistory"))
            {
                addViewToLayoutHome("ATTENDANCE HISTORY", R.mipmap.ic_histories4, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase("medical"))
            {
                addViewToLayoutHome("MEDICAL", R.mipmap.ic_medical4, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase("correction"))
            {
                addViewToLayoutHome("CORRECTION", R.mipmap.ic_correction4, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase("leaveRequest"))
            {
                addViewToLayoutHome("LEAVE\nREQUEST", R.mipmap.ic_leave_request4, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase("leaveCancelation"))
            {
                addViewToLayoutHome("LEAVE\nCANCELATION", R.mipmap.ic_leave_cancelation4, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase("signOut"))
            {
                addViewToLayoutHome("SIGN OUT", R.mipmap.ic_logout2, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase("transportReimburse"))
            {
                addViewToLayoutHome("TRANSPORT\nREIMBURSEMENT", R.mipmap.ic_reimburse2, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase("overtimeReimburse"))
            {
                addViewToLayoutHome("OVERTIME\nREIMBURSEMENT", R.mipmap.ic_reimburse2, hshLytHome, c-1, string);
            }
            else if(string.equalsIgnoreCase(""))
            {
                addViewToLayoutHome("", 0, hshLytHome, c-1, string);
            }
            c++;
        }

        List<Integer> forSort = new ArrayList<>();
        for(HashMap.Entry<Integer, LinearLayout> hashMap: hshLytHome.entrySet())
        {
            forSort.add(hashMap.getKey());
        }
        Collections.sort(forSort);
        for(Integer integer:forSort)
        {
            lytHome.addView(hshLytHome.get(integer));
        }
    }

    private void addViewToLayoutHome(String title, int icon, HashMap<Integer, LinearLayout> hshLytHome, int c, final String string)
    {
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(string.equalsIgnoreCase("attendance"))
                {
                    showStartEndDate();
                }
                else if(string.equalsIgnoreCase("outOfOffice"))
                {
                    showCheckInOut();
                }
                else if(string.equalsIgnoreCase("attendanceHistory"))
                {
                    showHistory();
                }
                else if(string.equalsIgnoreCase("medical"))
                {
                    showMedicalClaim();
                }
                else if(string.equalsIgnoreCase("correction"))
                {
                    showCorrection();
                }
                else if(string.equalsIgnoreCase("leaveRequest"))
                {
                    showLeaveRequest();
                }
                else if(string.equalsIgnoreCase("leaveCancelation"))
                {
                    showCancelation();
                }
                else if(string.equalsIgnoreCase("signOut"))
                {
                    signOut();
                }
                else if(string.equalsIgnoreCase(""))
                {

                }
                else if(string.equalsIgnoreCase("transportReimburse"))
                {
                    showTransportReimbursement();
                }
                else if(string.equalsIgnoreCase("overtimeReimburse"))
                {
                    showOvertimeReimbursement();
                }
            }
        });

        ImageView imageView=new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.spacing_63dp)));
        if(icon!=0)
            imageView.setImageResource(icon);

        TextView textView=new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        textView.setGravity(Gravity.CENTER);
        textView.setText(title);
        textView.setTextColor(getResources().getColor(android.R.color.background_light));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

        linearLayout.addView(imageView);
        linearLayout.addView(textView);

        hshLytHome.get(c/3).addView(linearLayout);
    }

    private void setToolbar()
    {
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.backgroundHome));
        }
    }

    void loadingImage(){


        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // khusus logType di hardcode -> LogType -> Start Day
        Call<GetImage> call3 = apiInterface.getImage();
        call3.enqueue(new Callback<GetImage>() {

            @Override
            public void onResponse(Call<GetImage> call, Response<GetImage> response) {

                GetImage data = response.body();

                if (data != null && data.getIsSucceed()) {
                    //Toast.makeText(HomeActivity.this, "Image terambil", Toast.LENGTH_LONG).show();
                    //finish();
                    byte[] decodedString = Base64.decode(data.getReturnValue(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageView.setImageBitmap(bitmap);

                } else {
                    try {
                        //JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(HomeActivity.this, "Session Expired", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    session.logoutUser();
                    progressDialog.dismiss();
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetImage> call, Throwable t) {
                Toast.makeText(HomeActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
//                session.logoutUser();
//                finish();
            }
        });
    }

    public void getEmployeeSchedule()
    {
        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
//        progressDialog = new ProgressDialog(HomeActivity.this);
//        progressDialog.setTitle("Loading");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        // khusus logType di hardcode -> LogType -> Start Day

        JSONObject paramObject= new JSONObject();
        try {

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date date=new Date();
            Date date7=new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 7);
            date7 = c.getTime();
            String startDate="";
            String endDate="";
            try
            {
                startDate=sdf.format(date);
                endDate=sdf.format(date7);
            }catch (Exception e)
            {

            }
            paramObject.put("StartDate",startDate);
            paramObject.put("EndDate",endDate);

        }catch (Exception e)
        {

        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),paramObject.toString());

        Call<EmployeeShiftSchedule> call3 = apiInterface.getEmployeeShiftSchedule(body);
        call3.enqueue(new Callback<EmployeeShiftSchedule>() {

            @Override
            public void onResponse(Call<EmployeeShiftSchedule> call, Response<EmployeeShiftSchedule> response) {

                if(response.isSuccessful())
                {
                    if(response.body().getIsSucceed())
                    {
                        Gson gson=new Gson();
                        SharedPreferenceUtils.setSetting(getApplicationContext(),"employeeSchedule", gson.toJson(response.body()) );
                    }
                    else
                    {
                        SharedPreferenceUtils.setSetting(getApplicationContext(),"employeeSchedule", "" );
                    }
                }
                else
                {
                    SharedPreferenceUtils.setSetting(getApplicationContext(),"employeeSchedule", "" );
                }
//                    progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<EmployeeShiftSchedule> call, Throwable t) {
                Toast.makeText(HomeActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
//                session.logoutUser();
//                finish();
            }
        });
    }

    public void showStartEndDate() {
        if (attendancePrivilege.equals("False")&&attendancePrivilege!=null){
            AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
            alert.setTitle("Alert");
            alert.setTitle("You do not have privilege to access this menu");
            alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            alert.show();
        } else {
            if(checkPermissionLocation())
            {
                Intent startEndDay = new Intent(this, StartEndDayActivity.class);
                startActivity(startEndDay);
            }
        }
    }

    public void showCheckInOut() {
        if(checkPermissionLocation())
        {
            Intent checkInOut = new Intent(this, CheckInOutActivity.class);
            startActivity(checkInOut);
        }

//        }
    }

    public void showHistory() {
        Intent histories = new Intent(this, HistoryFilterActivity.class);
        startActivity(histories);
    }

    public void showCorrection(){
        Intent correction = new Intent(this, CorrectionFilterActivity.class);
        startActivity(correction);
    }
    public void signOut(){
        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setTitle("Confirmation");
        alert.setTitle("Are You Sure to Sign Out?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                session.logoutUser();
                finish();
            }
        });

        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();

    }

    public void showOvertime(){
        Intent overtime = new Intent(this, OvertimeActivity.class);
        startActivity(overtime);

    }

    public void showCancelation(){
        Intent cancelation = new Intent(this, LeaveCancelationActivity.class);
        startActivity(cancelation);
    }

    public void showLeaveRequest() {
        Intent leaveRequest = new Intent(this, LeaveRequestActivity.class);
        startActivity(leaveRequest);
    }

    public void showMedicalClaim(){
        Intent medicalClaim = new Intent(this, MedicalClaimActivity.class);
        startActivity(medicalClaim);
    }

    public void showReimburse(){
        Intent reimburse = new Intent(this, ReimburseActivity.class);
        startActivity(reimburse);
    }

    public void showShiftExchange(){
        Intent shiftExchange = new Intent(this, ShiftExchangeActivity.class);
        startActivity(shiftExchange);
    }

    public void showLoan(){
        Intent shiftExchange = new Intent(this, LoanSelectorActivity.class);
        startActivity(shiftExchange);
    }

    public void showBeacon(View view){
        Intent beacon = new Intent(this, BeaconActivity.class);
        startActivity(beacon);
    }

    public boolean checkPermissionLocation()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
            return false;
        }
        return true;
    }
    //pindah ke halaman transportreimbursement
    public void showTransportReimbursement() {
        startActivity(new Intent(this,TransportReimbursementActivity.class));
    }
    //pindah ke halaman overtimereimbursement
    public void showOvertimeReimbursement() {
        startActivity(new Intent(this,OvertimeReimbursementActivity.class));
    }
    //pindah ke halaman entertainreimbursement
    public void showEntertainReimbursement(View view) {
        startActivity(new Intent(this,EntertainReimbursementActivity.class));
    }

}
