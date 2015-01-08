package com.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Gravity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Matrix;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.res.Resources;
import android.content.DialogInterface;
import android.widget.AbsoluteLayout;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import model.Cards;
import model.Player;
import model.PlayerAction;
import model.Wonder;
import model.WonderStage;
import model.ResourceType;
import controller.Controller;
import view.CardView;

public class SevenWonders extends Activity implements CardView {
	int width,height;
	TextView coinText,clayText,oreText,stoneText,woodText,glassText,loomText,papyrusText,shieldText,wonderText;
	Controller con;
	Object lock = new Object();
	Handler handler = new Handler();
	int numPlayers,playerCounter;
	int currentCard,currentTurn,currentAge;
	ArrayList<Cards> cards;
	int[] playableCost;
	Player p;
	ArrayList<View> resourceIcons;
	ArrayList<View> cardViews;
	ArrayList<View> descriptionIcons;
	ArrayList<View> messageIcons;
	ArrayList<View> previousIcons;
	ImageView cardDescription,wonderDescription;
	TextView cardDescriptionText,ageText,turnText,idText,nameText,messageText;
	Button playButton,wonderButton,okButton;
	AbsoluteLayout al;
	int topMargin=30;
	String resourceString="",commerceString="",eventInfo="";
	boolean isWonderBSide=false;
	Thread controllerThread;
	int defaultNumPlayers,defaultWonderSide,defaultWonder;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight()-getStatusBarHeight();
		
		setPreferences();
		getPreferences();
		al = (AbsoluteLayout) findViewById(R.id.layout1);
//		displayCardIcon();
		displayResourceIcon();
		displayDescriptionIcons();
		displayMessageIcon();
		displayWonderDescription();
		Controller.manualSimulation=true;
		final CardView cv = this;
		controllerThread = new Thread(new Runnable(){
			public void run() {
				con=new Controller(cv);
			}
		});
		selectNumPlayers();
//		controllerThread.start();
//		con.newGame(7);
    }

	public void getPreferences() {
		SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
		defaultNumPlayers = pref.getInt("numPlayers",-1);
		defaultWonderSide = pref.getInt("wonderSide",-1);
		defaultWonder = pref.getInt("wonder",-1);
	}

	public void setPreferences() {
		SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("numPlayers",7);
		editor.putInt("wonderSide",0);
		editor.putInt("wonder",0);
		editor.commit();
	}

	public int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
		      result = getResources().getDimensionPixelSize(resourceId);
		  } 
		  return result;
	}

	public void displayWonderDescription() {
		wonderDescription = new ImageView(this);
		wonderDescription.setLayoutParams(new AbsoluteLayout.LayoutParams(width,height,0,0));
		wonderDescription.setVisibility(View.GONE);
		wonderDescription.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				for (View v:previousIcons) v.setVisibility(View.VISIBLE);
				wonderDescription.setVisibility(View.GONE);
			}
		});
		al.addView(wonderDescription);
	}

	public void displayResources(Player pp) {
		p=pp;
		final Activity cv = this;
		handler.post(new Runnable(){
			public void run() {
				idText.setText("Player "+p.id);
				if (p.isWonderBSide) nameText.setText(p.wonder.name+"(B)");
				else nameText.setText(p.wonder.name+"(A)");
				coinText.setText(""+p.numCoin);
				clayText.setText(""+p.numClay);
				oreText.setText(""+p.numOre);
				stoneText.setText(""+p.numStone);
				woodText.setText(""+p.numWood);
				glassText.setText(""+p.numGlass);
				loomText.setText(""+p.numLoom);
				papyrusText.setText(""+p.numPapyrus);
				shieldText.setText(""+p.numShield);
				wonderText.setText(""+p.numWonderStages);
				if (p.resourceDescription.length()>resourceString.length()) {
					Resources res = getResources();
					for (String s:p.resourceDescription.substring(resourceString.length(),p.resourceDescription.length()).split(",")) {
						resourceString+=s+",";
						int vid = res.getIdentifier(s.toLowerCase().replaceAll("/","")+"_resource", "drawable", getApplicationContext().getPackageName());
						ImageView i1 = new ImageView(cv);
						i1.setLayoutParams(new AbsoluteLayout.LayoutParams(60,30,0,topMargin+350+(resourceIcons.size()-24)*35));
						i1.setImageResource(vid);
						al.addView(i1);
						resourceIcons.add(i1);
					}
				}
				if (p.commerceDescription.length()>commerceString.length()) {
					Resources res = getResources();
					for (String s:p.commerceDescription.substring(commerceString.length(),p.commerceDescription.length()).split(",")) {
						commerceString+=s+",";
						int vid = res.getIdentifier(s.toLowerCase().replaceAll("/","")+"_resource", "drawable", getApplicationContext().getPackageName());
						ImageView i1 = new ImageView(cv);
						i1.setLayoutParams(new AbsoluteLayout.LayoutParams(60,30,0,topMargin+350+(resourceIcons.size()-24)*35));
						i1.setImageResource(vid);
						al.addView(i1);
						resourceIcons.add(i1);
					}
				}
			}
		});
	}

	public void displayResourceIcon() {
		resourceIcons = new ArrayList<View>();
		ImageView i1 = new ImageView(this);
		i1.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin));
		i1.setImageResource(R.drawable.coin_resource);
		al.addView(i1);
		ImageView i2 = new ImageView(this);
		i2.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin+35));
		i2.setImageResource(R.drawable.clay_resource);
		al.addView(i2);
		ImageView i3 = new ImageView(this);
		i3.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin+70));
		i3.setImageResource(R.drawable.ore_resource);
		al.addView(i3);
		ImageView i4 = new ImageView(this);
		i4.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin+105));
		i4.setImageResource(R.drawable.stone_resource);
		al.addView(i4);
		ImageView i5 = new ImageView(this);
		i5.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin+140));
		i5.setImageResource(R.drawable.wood_resource);
		al.addView(i5);
		ImageView i6 = new ImageView(this);
		i6.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin+175));
		i6.setImageResource(R.drawable.glass_resource);
		al.addView(i6);
		ImageView i7 = new ImageView(this);
		i7.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin+210));
		i7.setImageResource(R.drawable.loom_resource);
		al.addView(i7);
		ImageView i8 = new ImageView(this);
		i8.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin+245));
		i8.setImageResource(R.drawable.papyrus_resource);
		al.addView(i8);
		ImageView i9 = new ImageView(this);
		i9.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin+280));
		i9.setImageResource(R.drawable.shield_resource);
		al.addView(i9);
		ImageView i10 = new ImageView(this);
		i10.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,0,topMargin+315));
		i10.setImageResource(R.drawable.wonder_resource);
		al.addView(i10);
		coinText = new TextView(this);
		coinText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin));
		coinText.setTextColor(Color.BLACK);
		al.addView(coinText);
		clayText = new TextView(this);
		clayText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin+35));
		clayText.setTextColor(Color.BLACK);
		al.addView(clayText);
		oreText = new TextView(this);
		oreText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin+70));
		oreText.setTextColor(Color.BLACK);
		al.addView(oreText);
		stoneText = new TextView(this);
		stoneText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin+105));
		stoneText.setTextColor(Color.BLACK);
		al.addView(stoneText);
		woodText = new TextView(this);
		woodText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin+140));
		woodText.setTextColor(Color.BLACK);
		al.addView(woodText);
		glassText = new TextView(this);
		glassText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin+175));
		glassText.setTextColor(Color.BLACK);
		al.addView(glassText);
		loomText = new TextView(this);
		loomText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin+210));
		loomText.setTextColor(Color.BLACK);
		al.addView(loomText);
		papyrusText = new TextView(this);
		papyrusText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin+245));
		papyrusText.setTextColor(Color.BLACK);
		al.addView(papyrusText);
		shieldText = new TextView(this);
		shieldText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin+280));
		shieldText.setTextColor(Color.BLACK);
		al.addView(shieldText);
		wonderText = new TextView(this);
		wonderText.setLayoutParams(new AbsoluteLayout.LayoutParams(30,30,40,topMargin+315));
		wonderText.setTextColor(Color.BLACK);
		al.addView(wonderText);
		idText = new TextView(this);
		idText.setTextColor(Color.BLACK);
		idText.setLayoutParams(new AbsoluteLayout.LayoutParams(width/5,topMargin,0,0));
		idText.setGravity(Gravity.CENTER);
		al.addView(idText);
		nameText = new TextView(this);
		nameText.setTextColor(Color.BLACK);
		nameText.setLayoutParams(new AbsoluteLayout.LayoutParams(width/5*2,topMargin,width/5,0));
		nameText.setGravity(Gravity.CENTER);
		al.addView(nameText);
		ageText = new TextView(this);
		ageText.setTextColor(Color.BLACK);
		ageText.setLayoutParams(new AbsoluteLayout.LayoutParams(width/5,topMargin,width/5*3,0));
		ageText.setGravity(Gravity.CENTER);
		al.addView(ageText);
		turnText = new TextView(this);
		turnText.setTextColor(Color.BLACK);
		turnText.setLayoutParams(new AbsoluteLayout.LayoutParams(width/5,topMargin,width/5*4,0));
		turnText.setGravity(Gravity.CENTER);
		al.addView(turnText);
//		Button btn1 = new Button(this);
//		btn1.setText("Wonder");
//		btn1.setLayoutParams(new AbsoluteLayout.LayoutParams(75,50,width-75,topMargin));
//		btn1.setOnClickListener(new OnClickListener() {	 
//			public void onClick(View arg0) {
//				System.out.println("Wonder");
//			}
//		});
//		al.addView(btn1);
//		Button btn2 = new Button(this);
//		btn2.setText("Discard");
//		btn2.setLayoutParams(new AbsoluteLayout.LayoutParams(75,50,width-75,topMargin+55));
//		btn2.setOnClickListener(new OnClickListener() {	 
//			public void onClick(View arg0) {
//				System.out.println("Discard");
//			}
//		});
//		al.addView(btn2);
		resourceIcons.add(i1);
		resourceIcons.add(i2);
		resourceIcons.add(i3);
		resourceIcons.add(i4);
		resourceIcons.add(i5);
		resourceIcons.add(i6);
		resourceIcons.add(i7);
		resourceIcons.add(i8);
		resourceIcons.add(i9);
		resourceIcons.add(i10);
		resourceIcons.add(coinText);
		resourceIcons.add(clayText);
		resourceIcons.add(oreText);
		resourceIcons.add(stoneText);
		resourceIcons.add(woodText);
		resourceIcons.add(glassText);
		resourceIcons.add(loomText);
		resourceIcons.add(papyrusText);
		resourceIcons.add(shieldText);
		resourceIcons.add(wonderText);
		resourceIcons.add(idText);
		resourceIcons.add(nameText);
		resourceIcons.add(ageText);
		resourceIcons.add(turnText);
//		resourceIcons.add(btn1);
//		resourceIcons.add(btn2);
	}

	public void displayCardIcon() {
		int cardWidth=width/2;
		int cardHeight=70;
		cardViews=new ArrayList<View>();
		for (int i=0;i<7;i++) {
			final int j=i;
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(new AbsoluteLayout.LayoutParams(cardWidth,cardHeight,cardWidth/2,topMargin+cardHeight*i));
			iv.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0) {
					displayCardDescription(j);
				}
			});
			al.addView(iv);
			cardViews.add(iv);
		}
//		for (int i=0;i<7;i++) {
//			TextView tv = new TextView(this);
//			tv.setTextColor(Color.BLACK);
//			tv.setLayoutParams(new AbsoluteLayout.LayoutParams(30,cardHeight,width/4*3,topMargin+cardHeight*i));
//			tv.setGravity(Gravity.CENTER);
//			al.addView(tv);
//			cardViews.add(tv);
//		}
	}

	public void displayMessageIcon() {
		messageIcons = new ArrayList<View>();
		messageText = new TextView(this);
		messageText.setTextColor(Color.BLACK);
		messageText.setLayoutParams(new AbsoluteLayout.LayoutParams(width-50,height/4*3,25,0));
		messageText.setGravity(Gravity.CENTER_VERTICAL);
		al.addView(messageText);
		messageIcons.add(messageText);
		okButton = new Button(this);
		okButton.setText("OK");
		okButton.setLayoutParams(new AbsoluteLayout.LayoutParams(80,40,width/2-40,height/8*7-20));
		al.addView(okButton);
		messageIcons.add(okButton);
		for (View v:messageIcons) v.setVisibility(View.GONE);
	}

	public void displayText(String s,OnClickListener listener) {
		final String ss = s;
		final OnClickListener l = listener;
		handler.post(new Runnable(){
			public void run() {
				messageText.setText(ss);
				okButton.setOnClickListener(l);
				for (View v:messageIcons) v.setVisibility(View.VISIBLE);
			}
		});
		synchronized(lock) {
			try {lock.wait();}
			catch (InterruptedException e) {}
		}
	}

	public void displayCards(ArrayList<Cards> c_,int[] playableCost_) {
		cards = c_;
		playableCost = playableCost_;
		final Activity cv = this;
		handler.post(new Runnable(){
			public void run() {
				int cardWidth=width/2;
				int cardHeight=70;
				cardViews=new ArrayList<View>();
				int numCards=cards.size();
				for (int i=0;i<numCards;i++) {
					final int j=i;
					ImageView iv = new ImageView(cv);
					iv.setLayoutParams(new AbsoluteLayout.LayoutParams(cardWidth,cardHeight,cardWidth/2,topMargin+cardHeight*i));
					iv.setOnClickListener(new OnClickListener(){
						public void onClick(View arg0) {
							displayCardDescription(j);
						}
					});
					al.addView(iv);
					cardViews.add(iv);
					Resources res = getResources();
		//			int id = res.getIdentifier("imageView"+i, "id", getApplicationContext().getPackageName());
					int vid = res.getIdentifier(cards.get(i).name.toLowerCase().replaceAll(" ","_"), "drawable", getApplicationContext().getPackageName());
		//			ImageView iv = (ImageView) findViewById(id);
		//			iv.setScaleType(ImageView.ScaleType.FIT_XY);
//					ImageView iv = (ImageView) cardViews.get(i);
//					if (iv.getDrawable()!=null) ((BitmapDrawable)iv.getDrawable()).getBitmap().recycle();
//					iv.setImageDrawable(null);
					iv.setImageResource(vid);
//					iv.setVisibility(View.VISIBLE);
				}
				for (int i=0;i<numCards;i++) {
//						TextView tv = (TextView) cardViews.get(i+7);
					TextView tv = new TextView(cv);
					tv.setTextColor(Color.BLACK);
					tv.setLayoutParams(new AbsoluteLayout.LayoutParams(30,cardHeight,width/4*3,topMargin+cardHeight*i));
					tv.setGravity(Gravity.CENTER);
					al.addView(tv);
					cardViews.add(tv);
					if (playableCost[i]!=0) {
						if (playableCost[i]<0) tv.setText("x");
						else if (playableCost[i]>0) tv.setText(""+playableCost[i]);
					}
				}
			}
		});
	}

	public void displayDescriptionIcons() {
		descriptionIcons = new ArrayList<View>();
		playButton = new Button(this);
		playButton.setText("Play");
		playButton.setLayoutParams(new AbsoluteLayout.LayoutParams(80,50,width/8-40,height/8*7-25));
		playButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (p.hasFreeBuild>0&&playableCost[currentCard]!=0) {
					selectFreeBuild();
					return;
				}
				if (playableCost[currentCard]>0) {
					selectTrading(p,p.resourceOptions.get(currentCard));
					return;
				}
				p.lastCard=cards.remove(currentCard);
//				for (View v:cardViews) v.setVisibility(View.GONE);
				for (View v:cardViews) al.removeView(v);
				for (View v:descriptionIcons) v.setVisibility(View.GONE);
//				for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
				al.removeView(cardDescription);
				synchronized(lock) {
					lock.notify();
				}
			}
		});
		al.addView(playButton);
		descriptionIcons.add(playButton);
		wonderButton = new Button(this);
		wonderButton.setText("Wonder");
		wonderButton.setLayoutParams(new AbsoluteLayout.LayoutParams(80,50,width/8*3-40,height/8*7-25));
		wonderButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				p.action=PlayerAction.WONDER;
				if (p.wonderOptions!=null) {
					selectTrading(p,p.wonderOptions);
					return;
				}
				p.lastCard=cards.remove(currentCard);
				for (View v:cardViews) al.removeView(v);
				for (View v:descriptionIcons) v.setVisibility(View.GONE);
//				for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
				al.removeView(cardDescription);
				synchronized(lock) {
					lock.notify();
				}
			}
		});
		al.addView(wonderButton);
		descriptionIcons.add(wonderButton);
		Button discardButton = new Button(this);
		discardButton.setText("Discard");
		discardButton.setLayoutParams(new AbsoluteLayout.LayoutParams(80,50,width/8*5-40,height/8*7-25));
		discardButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				p.lastCard=cards.remove(currentCard);
				p.action=PlayerAction.COIN;
				for (View v:cardViews) al.removeView(v);
				for (View v:descriptionIcons) v.setVisibility(View.GONE);
//				for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
				al.removeView(cardDescription);
				synchronized(lock) {
					lock.notify();
				}
			}
		});
		al.addView(discardButton);
		descriptionIcons.add(discardButton);
		Button backButton = new Button(this);
		backButton.setText("Back");
		backButton.setLayoutParams(new AbsoluteLayout.LayoutParams(80,50,width/8*7-40,height/8*7-25));
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				for (View v:descriptionIcons) v.setVisibility(View.GONE);
				for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
				for (int i=0;i<cards.size()*2;i++) cardViews.get(i).setVisibility(View.VISIBLE);
				al.removeView(cardDescription);
			}
		});
		al.addView(backButton);
		descriptionIcons.add(backButton);
//		cardDescription = new ImageView(this);
//		cardDescription.setLayoutParams(new AbsoluteLayout.LayoutParams(width,height/2,0,0));
//		al.addView(cardDescription);
//		descriptionIcons.add(cardDescription);
		cardDescriptionText = new TextView(this);
		cardDescriptionText.setTextColor(Color.BLACK);
		cardDescriptionText.setLayoutParams(new AbsoluteLayout.LayoutParams(width,height/4,0,height/2));
		al.addView(cardDescriptionText);
		descriptionIcons.add(cardDescriptionText);
		for (View v:descriptionIcons) v.setVisibility(View.GONE);
	}

	public void displayCardDescription(int i) {
		currentCard=i;
		Cards c = cards.get(i);
		Resources res = getResources();
		int vid = res.getIdentifier(c.name.toLowerCase().replaceAll(" ","_"), "drawable", getApplicationContext().getPackageName());
//		if (cardDescription.getDrawable()!=null) ((BitmapDrawable)cardDescription.getDrawable()).getBitmap().recycle();
//		cardDescription.setImageDrawable(null);
		cardDescription = new ImageView(this);
		cardDescription.setLayoutParams(new AbsoluteLayout.LayoutParams(width,height/2,0,0));
		al.addView(cardDescription);
//		descriptionIcons.add(cardDescription);
		cardDescription.setImageResource(vid);
		String s = c.getDescription();
		playButton.setEnabled(false);
		if (playableCost[i]==-2) s+="\nCannot build 2 identical structures";
		else if (playableCost[i]==-1) s+="\nNot enough resources to build";
		else playButton.setEnabled(true);
		if (p.hasFreeBuild>0) playButton.setEnabled(true);
		if (p.canBuildWonder) wonderButton.setEnabled(true);
		else {
			wonderButton.setEnabled(false);
			WonderStage[] wonderSide;
			if (p.isWonderBSide) wonderSide=p.wonder.stagesB;
			else wonderSide=p.wonder.stagesA;
			if (p.numWonderStages>=wonderSide.length) s+="\nWonder already complete";
			else s+="\nNot enough resources to construct wonder";
		}
		cardDescriptionText.setText(s);
		for (View v:cardViews) v.setVisibility(View.GONE);
		for (View v:resourceIcons) v.setVisibility(View.GONE);
		for (View v:descriptionIcons) v.setVisibility(View.VISIBLE);
	}

	public void message(String s){
		final Activity c = this;
		final String ss = s;
		handler.post(new Runnable(){
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
				builder.setMessage(ss)
					.setCancelable(false)
	          		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				             public void onClick(DialogInterface dialog, int id) {
								synchronized(lock) {
									lock.notify();
								}
							}
					 });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		synchronized(lock) {
			try {lock.wait();}
			catch (InterruptedException e) {}
		}
	}

	public void displayWonders(Wonder[] w){
		numPlayers=w.length;
	}
	
	public void setWonder() {
		String s = p.wonder.name.toLowerCase()+"_";
		if (isWonderBSide) s+="b";
		else s+="a";
		System.out.println(s);
		Resources res = getResources();
		int vid = res.getIdentifier(s, "drawable", getApplicationContext().getPackageName());
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Bitmap myImg = BitmapFactory.decodeResource(getResources(),vid);
		myImg = Bitmap.createScaledBitmap(myImg,height,width,true);
		final Bitmap rotated =  Bitmap.createBitmap(myImg,0,0,myImg.getWidth(),myImg.getHeight(),matrix,true);
		handler.post(new Runnable(){
			public void run() {
				wonderDescription.setImageBitmap(rotated);
			}
		});
	}

	public void displayAge(int age_){
		currentAge = age_;
		handler.post(new Runnable(){
			public void run() {
				ageText.setText("Age "+currentAge);
			}
		});
	}

	public void displayTurn(int turn_){
		currentTurn = turn_;
		handler.post(new Runnable(){
			public void run() {
				turnText.setText("Turn "+currentTurn);
			}
		});
	}

	public void displayScore(Player[] p,ArrayList<Integer> winner,ArrayList<Integer> totalScore,int[][] scoreCategories){
		String s = "";
		s+=" | BR GY Y  BL GN R  P  | V  D \n";
		for (Player pp:p) {
			s+=String.format("%1d| %2d %2d %2d %2d %2d %2d %2d| %2d %2d\n",pp.id,pp.numBrown,pp.numGray,pp.numYellow,pp.numBlue,pp.numGreen,pp.numRed,pp.numPurple,pp.victoryToken,pp.defeatToken);
		}
		s+="\n | Mil Cn Won Civ Sci Com Gld | Sum\n";
		for (int i=0;i<p.length;i++) {
			s+=String.format("%1d| %3d %2d %3d %3d %3d %3d %3d | %3d\n",i,
				scoreCategories[0][i],
				scoreCategories[1][i],
				scoreCategories[2][i],
				scoreCategories[3][i],
				scoreCategories[4][i],
				scoreCategories[5][i],
				scoreCategories[6][i],
				totalScore.get(i));
		}
		if (winner.size()==1) s+="\nThe winner is Player "+winner.get(0)+"!\n";
		else {
			s+="The winners are";
			for (Integer i:winner) {
				s+=" Player "+i;
			}
			s+="!\n";
		}
//		final Activity c = this;
//		final String ss = s;
//		handler.post(new Runnable(){
//			public void run() {
//				AlertDialog.Builder builder = new AlertDialog.Builder(c);
//				builder.setMessage(ss)
//	          		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//				             public void onClick(DialogInterface dialog, int id) {
//								Intent intent = getIntent();
//								finish();
//								startActivity(intent);
//							}
//					   });
//				AlertDialog alert = builder.create();
//				alert.show();
//			}
//		});
		displayText(s,new OnClickListener(){
			public void onClick(View arg0) {
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});
	}

	public void displayPayment(String src,String dest,int amount){
		eventInfo+=src+" payed "+amount+" coin to "+dest+"\n";
	}

	public void showDiscardAction(String src){
		playerCounter++;
		eventInfo+=src+" discarded a card for 3 coin\n";
		if (playerCounter==numPlayers) {
			playerCounter=0;
//			message(eventInfo);
			displayText(eventInfo,new OnClickListener() {
				public void onClick(View arg0) {
					if (currentTurn!=6) {
						for (View v:messageIcons) v.setVisibility(View.GONE);
						for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
					}
					synchronized(lock) {
						lock.notify();
					}
				}
			});
			eventInfo="";
		}
	}

	public void showWonderAction(WonderStage w,int numWonderStages,String name){
		playerCounter++;
		eventInfo+=name+" built Wonder Stage "+numWonderStages+" ";
		if (w.numCoin>0) {
			eventInfo+="+"+w.numCoin+" COIN ";
		}
		if (w.numShield>0) {
			eventInfo+="+"+w.numShield+" SHIELD ";
		}
		eventInfo+="\n";
		if (playerCounter==numPlayers) {
			playerCounter=0;
//			message(eventInfo);
			displayText(eventInfo,new OnClickListener() {
				public void onClick(View arg0) {
					if (currentTurn!=6) {
						for (View v:messageIcons) v.setVisibility(View.GONE);
						for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
					}
					synchronized(lock) {
						lock.notify();
					}
				}
			});
			eventInfo="";
		}
	}

	public void showCardAction(String cardName,int dCoin,String playerName){
		playerCounter++;
		eventInfo+=playerName+" played "+cardName;
		if (dCoin>0) eventInfo+=" for "+dCoin+" coin\n";
		else eventInfo+="\n";
		if (playerCounter==numPlayers) {
			playerCounter=0;
//			message(eventInfo);
			displayText(eventInfo,new OnClickListener() {
				public void onClick(View arg0) {
					if (currentTurn!=6) {
						for (View v:messageIcons) v.setVisibility(View.GONE);
						for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
					}
					synchronized(lock) {
						lock.notify();
					}
				}
			});
			eventInfo="";
		}
	} 

	public void displayWarResults(Player[] p,int[] warResult) {
		String s="War Results:\n\nPlayer | Shields Result\n";
		for (int i=0;i<p.length;i++) {
			s+=String.format("%6d | %7d %6d\n",i,p[i].numShield,warResult[i]);
		}
		displayText(s,new OnClickListener() {
			public void onClick(View arg0) {
				if (currentAge!=3||currentTurn!=6) {
					for (View v:messageIcons) v.setVisibility(View.GONE);
					for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
				}
				synchronized(lock) {
					lock.notify();
				}
			}
		});
	}

	public void selectWonderSide(Player pp){
		p=pp;
		if (defaultWonderSide>=0) {
			if (defaultWonderSide==1) {p.isWonderBSide=true; isWonderBSide=true;}
			setWonder();
			return;
		}
		final Activity c = this;
		handler.post(new Runnable(){
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
				builder.setMessage("Your wonder is "+p.wonder.name+". Please select a side:")
	          		.setPositiveButton("Side A", new DialogInterface.OnClickListener() {
				             public void onClick(DialogInterface dialog, int id) {
								 setWonder();
								synchronized(lock) {
									lock.notify();
								}
							}
					 })
	          		.setNegativeButton("Side B", new DialogInterface.OnClickListener() {
				             public void onClick(DialogInterface dialog, int id) {
								p.isWonderBSide=true;
								isWonderBSide=true;
								 setWonder();
								synchronized(lock) {
									lock.notify();
								}
							}
					 });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		synchronized(lock) {
			try {lock.wait();}
			catch (InterruptedException e) {}
		}
	}

	public void selectFreeBuild() {
		final Activity c = this;
		handler.post(new Runnable(){
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
				builder.setMessage("Use a free build?")
					.setCancelable(false)
	          		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				             public void onClick(DialogInterface dialog, int id) {
								p.hasFreeBuild=0;
								p.lastCard=cards.remove(currentCard);
								for (View v:cardViews) al.removeView(v);
								for (View v:descriptionIcons) v.setVisibility(View.GONE);
								al.removeView(cardDescription);
								synchronized(lock) {
									lock.notify();
								}
							}
					 })
	          		.setNegativeButton("No", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int id) {
								if (playableCost[currentCard]>0) {
									selectTrading(p,p.resourceOptions.get(currentCard));
								}
							}
					 });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	public void selectNumPlayers(){
		if (defaultNumPlayers>0) {
			Controller.defaultNumPlayers=defaultNumPlayers;
			controllerThread.start();
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
//			.setMessage("Select number of players:")
			.setTitle("Select number of players:")
			.setItems(new CharSequence[] {"3","4","5","6","7"}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						switch(id) {
							case 0:
								Controller.defaultNumPlayers=3;
								break;
							case 1:
								Controller.defaultNumPlayers=4;
								break;
							case 2:
								Controller.defaultNumPlayers=5;
								break;
							case 3:
								Controller.defaultNumPlayers=6;
								break;
							case 4:
								Controller.defaultNumPlayers=7;
								break;
						}
						controllerThread.start();
					}
			 });
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void selectTrading(Player pp, ArrayList<Integer> options_){
		p=pp;
		final ArrayList<Integer> options = options_;
		final Activity cv = this;
		handler.post(new Runnable(){
			public void run() {
				CharSequence[] labels = new CharSequence[options.size()+1];
				for (int i=0;i<options.size();i++) labels[i]="Left:"+(options.get(i)/100)+" Right:"+(options.get(i)%100);
				labels[options.size()]="Cancel";
				AlertDialog.Builder builder = new AlertDialog.Builder(cv);
				builder
					.setTitle("Select amount to purchase from neighbors:")
					.setCancelable(false)
					.setItems(labels, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (id==options.size()) {
									p.action=PlayerAction.CARD;
									return;
								}
								p.leftCost=options.get(id)/100;
								p.rightCost=options.get(id)%100;
								p.lastCard=cards.remove(currentCard);
								for (View v:cardViews) al.removeView(v);
								for (View v:descriptionIcons) v.setVisibility(View.GONE);
								al.removeView(cardDescription);
								synchronized(lock) {
									lock.notify();
								}
							}
					 });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	} 

	public void selectAction(Player p,ArrayList<Cards> cards){
		synchronized(lock) {
			try {lock.wait();}
			catch (InterruptedException e) {}
		}
	} 

	public void displayPlayerName(String s){}
	public void displayDiscardPile(ArrayList<Cards> discardPile){}
	public void displayNeighborResources(String name,Player p){}
	public void selectFromDiscard(Player p,ArrayList<Cards> discardPile, ArrayList<Cards> selection){}
	public void selectGuild(Player p,ArrayList<Cards> guildChoices){}
	public void selectLookAction(Player p,ArrayList<Cards> cards){} 
	public void selectCard(Player p,ArrayList<Cards> cards){}

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }
	
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		case R.id.cardsplayedmenu:
			return true;
		case R.id.wondermenu:
			if (wonderDescription.getVisibility()!=View.VISIBLE) {
				previousIcons=new ArrayList<View>();
				if (resourceIcons.get(0).getVisibility()==View.VISIBLE) {
					for (View v:resourceIcons) {v.setVisibility(View.GONE); previousIcons.add(v);}
					for (View v:cardViews) {v.setVisibility(View.GONE); previousIcons.add(v);}
				} else if (descriptionIcons.get(0).getVisibility()==View.VISIBLE) {
					for (View v:descriptionIcons) {v.setVisibility(View.GONE);previousIcons.add(v);}
					cardDescription.setVisibility(View.GONE);previousIcons.add(cardDescription);
				} else
					for (View v:messageIcons) {v.setVisibility(View.GONE);previousIcons.add(v);}
				wonderDescription.setVisibility(View.VISIBLE);
			}
			return true;
        case R.id.newgamemenu:
			Intent intent = getIntent();
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			finish();
			startActivity(intent);
            return true;
        case R.id.quitmenu:
			finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}

	@Override
	public void onBackPressed() {}

}
