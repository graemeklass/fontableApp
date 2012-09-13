package com.klassicapps.fontable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	 
	final static float STEP = 200;
	 AutoResizeTextView txtStatusOut;
	 EditText txtStatusIn;
	 float mRatio = 1.0f;
	 int mBaseDist;
	 float mBaseRatio;
	 float fontsize = 13;
	 LocationManager locationManager;
	 LocationListener locationListener = new mylocationlistener();
	 Location location;
	 String provider;
	 Typeface[] fontArray = new Typeface[10];
	 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        setTitle("");
        
        Criteria criteria = new Criteria();
      	 
	   	 criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	   	 criteria.setAltitudeRequired(false);
	   	 criteria.setBearingRequired(false);
	   	 criteria.setCostAllowed(true);
	   	 criteria.setPowerRequirement(Criteria.POWER_LOW);

	   	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	   	
	   	 provider = locationManager.getBestProvider(criteria, true);
	   	 
	   	locationManager.requestLocationUpdates(provider, 5000, 10, locationListener);
	   	
	   	location = locationManager.getLastKnownLocation(provider);
	   	
	   	
	   	
        
        fontArray[0] = Typeface.createFromAsset(getAssets(),
                "fonts/FFF_Tusj.ttf");
        fontArray[1] = Typeface.createFromAsset(getAssets(),
                "fonts/Oregano-Regular.ttf");
        fontArray[2] = Typeface.createFromAsset(getAssets(),
                "fonts/Code_Pro_Demo.ttf");
        fontArray[3] = Typeface.createFromAsset(getAssets(),
                "fonts/MuseoSans_500.ttf");
        fontArray[4] = Typeface.createFromAsset(getAssets(),
                "fonts/Impact_Label.ttf");
        fontArray[5] = Typeface.createFromAsset(getAssets(),
                "fonts/benjaminfranklin.ttf");
        fontArray[6] = Typeface.createFromAsset(getAssets(),
                "fonts/1776_independencenormal.ttf");
        fontArray[7] = Typeface.createFromAsset(getAssets(),
                "fonts/PiekosProfessionalBB_bold.ttf");
        fontArray[8] = Typeface.createFromAsset(getAssets(),
                "fonts/carbontype.ttf");
        fontArray[9] = Typeface.createFromAsset(getAssets(),
                "fonts/old_stamper.ttf");
        
        Button btnFont1 = (Button)findViewById(R.id.btnFont1);
        btnFont1.setTypeface(fontArray[0]);
        Button btnFont2 = (Button)findViewById(R.id.btnFont2);
        btnFont2.setTypeface(fontArray[1]);
        Button btnFont3 = (Button)findViewById(R.id.btnFont3);
        btnFont3.setTypeface(fontArray[2]);
        Button btnFont4 = (Button)findViewById(R.id.btnFont4);
        btnFont4.setTypeface(fontArray[3]);
        Button btnFont5 = (Button)findViewById(R.id.btnFont5);
        btnFont5.setTypeface(fontArray[4]);
        Button btnFont6 = (Button)findViewById(R.id.btnFont6);
        btnFont6.setTypeface(fontArray[5]);
        Button btnFont7 = (Button)findViewById(R.id.btnFont7);
        btnFont7.setTypeface(fontArray[6]);
        Button btnFont8 = (Button)findViewById(R.id.btnFont8);
        btnFont8.setTypeface(fontArray[7]);
        Button btnFont9 = (Button)findViewById(R.id.btnFont9);
        btnFont9.setTypeface(fontArray[8]);
        Button btnFont10 = (Button)findViewById(R.id.btnFont10);
        btnFont10.setTypeface(fontArray[9]);
        
        ImageButton btnShare = (ImageButton)findViewById(R.id.btnShare);
        
        btnFont1.setOnClickListener(handlerFont1);
    	btnFont2.setOnClickListener(handlerFont2);
    	btnFont3.setOnClickListener(handlerFont3);
    	btnFont4.setOnClickListener(handlerFont4);
    	btnFont5.setOnClickListener(handlerFont5);
    	btnFont6.setOnClickListener(handlerFont6);
    	btnFont7.setOnClickListener(handlerFont7);
    	btnFont8.setOnClickListener(handlerFont8);
    	btnFont9.setOnClickListener(handlerFont9);
    	btnFont10.setOnClickListener(handlerFont10);
    	
    	btnShare.setOnClickListener(shareImageText);
    	
    	txtStatusIn = (EditText)findViewById(R.id.txtStatusIn);
    	//txtStatusIn.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    	
    	txtStatusOut = (AutoResizeTextView)findViewById(R.id.txtStatusOut);
    	txtStatusOut.setTypeface(fontArray[0]); 
    	
    	txtStatusIn.addTextChangedListener(handlerTextChanged);
    	
    	fontsize = txtStatusOut.getTextSize(); 
    	
    	
    	
    	
    	txtStatusOut.setOnTouchListener(new TextView.OnTouchListener() {
    		 @Override
    		    public boolean onTouch(View v, MotionEvent event) {
    		        TextView tv = (TextView) v.findViewById(R.id.txtStatusOut);
    		        if (event.getPointerCount() == 2) {
    		 		   int action = event.getAction();
    		 		   int pureaction = action & MotionEvent.ACTION_MASK;
    		 		   if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
    		 		    mBaseDist = getDistance(event);
    		 		    mBaseRatio = mRatio;
    		 		   } else {
    		 		    float delta = (getDistance(event) - mBaseDist) / STEP;
    		 		    float multi = (float)Math.pow(2, delta);
    		 		    mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
    		 		   tv.setTextSize(mRatio*fontsize);
    		 		   tv.setGravity(Gravity.CENTER);
    		 		   
    		 		   }
    		 		  }
    		 		  return true; 
    		 		 }

    		 		 int getDistance(MotionEvent event) {
    		 		  int dx = (int)(event.getX(0) - event.getX(1));
    		 		  int dy = (int)(event.getY(0) - event.getY(1));
    		 		  return (int)(Math.sqrt(dx * dx + dy * dy));
    		 		 }
    	});
    	
    	//let's find out whether height or width is bigger and set the other to that height to make square
    	int height = txtStatusOut.getTextViewHeight();
    	int width = txtStatusOut.getTextViewWidth();
    	
    	int squareDim = Math.min(height, width);
    	txtStatusOut.getLayoutParams().height = squareDim;
    	txtStatusOut.getLayoutParams().width = squareDim;
    	
    	
    }
	
	// Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        case R.id.menu_settings:
            //show settings
        	Intent intent = new Intent(MainActivity.this, PrefsActivity.class);
            super.startActivity(intent);
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }    
	
	
	private class mylocationlistener implements LocationListener {
	    
		@Override
	    public void onLocationChanged(Location location) {
	        if (location != null) {
	        Log.d("LOCATION CHANGED", location.getLatitude() + "");
	        Log.d("LOCATION CHANGED", location.getLongitude() + "");
//	        Toast.makeText(MainActivity.this,
//	            location.getLatitude() + "" + location.getLongitude(),
//	            Toast.LENGTH_LONG).show();
	        }
	    }
	    @Override
	    public void onProviderDisabled(String provider) {
	    }
	    @Override
	    public void onProviderEnabled(String provider) {
	    }
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }
		
			
		}


	TextWatcher handlerTextChanged = new TextWatcher()
	{

		@Override
		public void afterTextChanged(Editable s) {
						
			txtStatusOut.setText(txtStatusIn.getText());
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			
		}
		
	};
	
	View.OnClickListener shareImageText = new View.OnClickListener() {
        public void onClick(View v) {
        	shareTextImage();
        	
        }
    };
        
    View.OnClickListener handlerFont1= new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut, fontArray[0]);
        	
        }
    };
    
    View.OnClickListener handlerFont2 = new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut, fontArray[1]);
        	
        }
    }; 
    
    View.OnClickListener handlerFont3 = new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut,fontArray[2]);
        	
        }
    }; 
    
    View.OnClickListener handlerFont4 = new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut, fontArray[3]);
        	
        }
    }; 
    
    View.OnClickListener handlerFont5 = new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut, fontArray[4]);
        	
        }
    }; 
    
    View.OnClickListener handlerFont6 = new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut, fontArray[5]);
        	
        }
    }; 
    
    View.OnClickListener handlerFont7 = new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut, fontArray[6]);
        	
        }
    }; 
    
    View.OnClickListener handlerFont8 = new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut, fontArray[7]);
        	
        }
    }; 
    
    View.OnClickListener handlerFont9 = new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut, fontArray[8]);
        	
        }
    }; 
    
    View.OnClickListener handlerFont10 = new View.OnClickListener() {
        public void onClick(View v) {
        	
            setStatusOut(txtStatusIn, txtStatusOut, fontArray[9]);
        	
        }
    }; 
    

   
	private void setStatusOut(EditText _txtStatusIn, TextView _txtStatusOut, Typeface font) {
		_txtStatusOut.setTypeface(font);            
		
		_txtStatusOut.setText(txtStatusIn.getText());
	}
	
	private void shareTextImage() {
		location = locationManager.getLastKnownLocation(provider);
		
		TextView txtStatusOut = (AutoResizeTextView)findViewById(R.id.txtStatusOut);
		Resources res = getResources();
		float scaledHeight = res.getDimension(R.dimen.facebookImageHeight);
		float scaledWidth = res.getDimension(R.dimen.facebookImageWidth);
    	
    	Bitmap bmpStatusOut = Bitmap.createScaledBitmap(loadBitmapFromView(txtStatusOut), (int)scaledWidth, (int)scaledHeight, true);
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
    	String date = dateFormat.format(new Date());
    	
    	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    	
    	bmpStatusOut.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    	
    	Intent share = new Intent(Intent.ACTION_SEND);
    	share.setType("image/jpg");
    	
    	String filename1 = txtStatusOut.getText().toString();
    	filename1 = filename1.replaceAll("[^a-zA-Z0-9]", "");
    	String filename = Environment.getExternalStorageDirectory() + File.separator + "fontable" + File.separator + "kerning_" + filename1 + "_" + date + ".jpg";
    	createDirIfNotExists("fontable");
    	File f = new File(filename);
    	try {
    	    f.createNewFile();
    	    FileOutputStream fo = new FileOutputStream(f);
    	    fo.write(bytes.toByteArray());
    	    fo.close();
    	    
    	    //check geotag setting, if true set it.
    	  //get data from settings activity in this case the language
    	    SharedPreferences appPrefs = 
                    getSharedPreferences("fontablePrefs", MODE_PRIVATE);
            //in the method getBoolean of geotag option. default is false.
            Boolean geotag = appPrefs.getBoolean("geotag", false);
    	    if (geotag)
    	    	saveGeotag(filename);

        	
        	share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+filename));
        	startActivity(Intent.createChooser(share, "share fontable..."));
    	} catch (IOException e) {                       
    	        e.printStackTrace();
    	}
    	
    	galleryAddImage(filename);
    	bmpStatusOut.recycle();
    	
	}

	private void saveGeotag(String filename) throws IOException {
		ExifInterface exif = new ExifInterface(filename);
		//String latitudeStr = "90/1,12/1,30/1";
		double lat = location.getLatitude();
		double alat = Math.abs(lat);
		String dms = Location.convert(alat, Location.FORMAT_SECONDS);
		String[] splits = dms.split(":");
		String[] secnds = (splits[2]).split("\\.");
		String seconds;
		if(secnds.length==0)
		{
		    seconds = splits[2];
		}
		else
		{
		    seconds = secnds[0];
		}

		String latitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
		exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitudeStr);

		exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, lat>0?"N":"S");

		double lon = location.getLongitude();
		double alon = Math.abs(lon);


		dms = Location.convert(alon, Location.FORMAT_SECONDS);
		splits = dms.split(":");
		secnds = (splits[2]).split("\\.");

		if(secnds.length==0)
		{
		    seconds = splits[2];
		}
		else
		{
		    seconds = secnds[0];
		}
		String longitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";


		exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitudeStr);
		exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon>0?"E":"W");

		exif.saveAttributes();
	}
	
	private void galleryAddImage(String imagePath) {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(imagePath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}
	
	public static boolean createDirIfNotExists(String path) {
	    boolean ret = true;

	    File file = new File(Environment.getExternalStorageDirectory(), path);
	    if (!file.exists()) {
	        if (!file.mkdirs()) {
	        	
	            ret = false;
	        }
	    }
	    return ret;
	}
	
	public static Bitmap loadBitmapFromView(View v) {
        Bitmap bmpStatus = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpStatus);
        
        v.draw(c);
        
        Bitmap bmpStatusWatermark = addWatermark(v.getContext().getResources(), bmpStatus);
        
        return bmpStatusWatermark;
        
    }
	
	public static Bitmap addWatermark(Resources res, Bitmap source) {
		  int w, h;
		  Canvas c;
		  Paint paint;
		  Bitmap bmp, watermark;

		  Matrix matrix;
		  float scale;
		  RectF r;

		  w = source.getWidth();
		  h = source.getHeight();

		  // Create the new bitmap
		  bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		  //bmp.setHasAlpha(true);
		  paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);

		  // Copy the original bitmap into the new one
		  c = new Canvas(bmp);
		  c.drawBitmap(source, 0, 0, paint);

		  // Load the watermark
		  watermark = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
		  // Scale the watermark to be approximately 10% of the source image height
		  scale = (float) (((float) h * 0.10) / (float) watermark.getHeight());

		  // Create the matrix
		  matrix = new Matrix();
		 
		  matrix.postScale(scale, scale);
		  // Determine the post-scaled size of the watermark
		  r = new RectF(0, 0, watermark.getWidth(), watermark.getHeight());
		  matrix.mapRect(r);
		  // Move the watermark to the bottom right corner
		  matrix.postTranslate(w - r.width() - 3, h - r.height() - 3);

		  // Draw the watermark
//		  Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
//		  paint2.setAlpha(0);
		  c.drawBitmap(watermark, matrix, paint);
		  
		  // Free up the bitmap memory
		  watermark.recycle();

		  return bmp;
		} 
	

	
} 
	
	
	


