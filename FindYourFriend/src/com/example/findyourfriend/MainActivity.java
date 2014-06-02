package com.example.findyourfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	
	
	//Declare
	private LinearLayout slidingPanel;
	private boolean isExpanded;
	private DisplayMetrics metrics;	
	private ListView listView;
	private RelativeLayout headerPanel;
	private RelativeLayout menuPanel;
	private int panelWidth;
	private ImageView menuViewButton;
	Button menu1 ;
	Button menu2 ;
	Button menu3;
	TextView txtpays;
	String[] listeStrings = {"Tunisie","Libye","Egypte","Yemen","Syrie"};
	String[] listeStrings2 = {"Mauritanie","Maroc","Algerie","Arabie saoudite","jordanie","Pays du golf"};
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters ;
	LinearLayout.LayoutParams listViewParameters;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide_view);
		
		//Initialize
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		panelWidth = (int) ((metrics.widthPixels)*0.75);
	
		headerPanel = (RelativeLayout) findViewById(R.id.header);
		headerPanelParameters = (LinearLayout.LayoutParams) headerPanel.getLayoutParams();
		headerPanelParameters.width = metrics.widthPixels;
		headerPanel.setLayoutParams(headerPanelParameters);
		
		menuPanel = (RelativeLayout) findViewById(R.id.menuPanel);
		menuPanelParameters = (FrameLayout.LayoutParams) menuPanel.getLayoutParams();
		menuPanelParameters.width = panelWidth;
		menuPanel.setLayoutParams(menuPanelParameters);
		
		slidingPanel = (LinearLayout) findViewById(R.id.slidingPanel);
		slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel.getLayoutParams();
		slidingPanelParameters.width = metrics.widthPixels;
		slidingPanel.setLayoutParams(slidingPanelParameters);
		
		listView = (ListView) findViewById(R.id.listView1);
		listViewParameters = (LinearLayout.LayoutParams) listView.getLayoutParams();
		listViewParameters.width = metrics.widthPixels;
		listView.setLayoutParams(listViewParameters);
		
	
	
		//Slide the Panel	
	 
	
	 	menu1 = (Button) findViewById(R.id.menu_item_1);	
		txtpays = (TextView) findViewById(R.id.listepays);	
	 	menu2 = (Button) findViewById(R.id.menu_item_2);
	 	menu1.setOnClickListener(this);
	 	menu2.setOnClickListener(this);
	 	menu3=(Button)findViewById(R.id.menu_item_3);
	 	menu3.setOnClickListener(this);
	 	
menuViewButton = (ImageView) findViewById(R.id.menuViewButton);
		
		menuViewButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	if(!isExpanded){
		    		isExpanded = true;   		    				        		
		        	
		    		//Expand
		    		new ExpandAnimation(slidingPanel, panelWidth,
		    	    Animation.RELATIVE_TO_SELF, 0.0f,
		    	    Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);		    			         	    
		    	}else{
		    		isExpanded = false;
		    		
		    		//Collapse
		    		new CollapseAnimation(slidingPanel,panelWidth,
            	    TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
            	    TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
		   
					
		    	}         	   
		    }
		});
	}
	 	
	 	@Override
		public void onClick(View v) {
			Intent intent;
			switch(v.getId()){
			case R.id.menu_item_1:
				intent=new Intent(this, ContactsActivity.class);
				startActivity(intent);
				break;
			case R.id.menu_item_2:
				intent = new Intent(this,FriendsActivity.class);
				startActivity(intent);
				break;
			case R.id.menu_item_3:
				intent = new Intent(this,LocationActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
	 	}
}
 
	 /*	menu1.setOnClickListener(new OnClickListener() {
	 		Intent intent;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				    txtpays.setText("Liste des pays arabes libres:");
				 	listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,listeStrings));
				  
		 
			}
		});
		 
	menu2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				
			 
				
			  
						
					 
						    txtpays.setText("Liste des pays arabes en attente:");
						 	listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,listeStrings2));
						  
			 
		 
			}
		});
		menuViewButton = (ImageView) findViewById(R.id.menuViewButton);
		
		menuViewButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	if(!isExpanded){
		    		isExpanded = true;   		    				        		
		        	
		    		//Expand
		    		new ExpandAnimation(slidingPanel, panelWidth,
		    	    Animation.RELATIVE_TO_SELF, 0.0f,
		    	    Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);		    			         	    
		    	}else{
		    		isExpanded = false;
		    		
		    		//Collapse
		    		new CollapseAnimation(slidingPanel,panelWidth,
            	    TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
            	    TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
		   
					
		    	}         	   
		    }
		});
				
	}	
	 
}*/








/*import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	Button btnFriendsActivity;
	Button btnFriends;
	Button btnLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnFriendsActivity=(Button)findViewById(R.id.btnFriendsActivity);
		btnFriendsActivity.setOnClickListener(this);
		btnFriends = (Button)findViewById(R.id.btnFriends);
		btnFriends.setOnClickListener(this);
		btnLocation = (Button)findViewById(R.id.btnLocation);
		btnLocation.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()){
		case R.id.btnFriendsActivity:
			intent=new Intent(this, ContactsActivity.class);
			startActivity(intent);
			break;
		case R.id.btnFriends:
			intent = new Intent(this,FriendsActivity.class);
			startActivity(intent);
			break;
		case R.id.btnLocation:
			intent = new Intent(this,LocationActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}

}*/
