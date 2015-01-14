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
import android.view.KeyEvent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Matrix;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.res.Resources;
import android.content.DialogInterface;
import android.widget.AbsoluteLayout;
import android.os.Handler;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
	Random random = new Random();
	int numPlayers;
	int currentCard,currentTurn,currentAge;
	ArrayList<Cards> cards;
	int[] playableCost;
	Player p;
	ArrayList<View> resourceIcons;
	ArrayList<View> dualResourceIcons;
	ArrayList<View> cardViews;
	ArrayList<View> neighborDualResourceIcons=new ArrayList<View>();
	ArrayList<View> neighborCardViews=new ArrayList<View>();
	ArrayList<View> descriptionIcons;
	ArrayList<View> messageIcons;
	ArrayList<View> previousIcons;
	RadioGroup radioIcons;
	RadioButton radioZero;
	ImageView cardDescription,wonderDescription;
	TextView cardDescriptionText,ageText,turnText,idText,nameText,messageText;
	Button playButton,wonderButton,okButton;
	AbsoluteLayout al;
	int topMargin=30;
	String resourceString="",commerceString="",eventInfo="";
	boolean isWonderBSide=false;
	Thread controllerThread;
	String defaultNumPlayers,defaultWonderSide,defaultWonder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight()-getStatusBarHeight();
		
		getPreferences();
		al = (AbsoluteLayout) findViewById(R.id.layout1);
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
    }

	public void getPreferences() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		defaultNumPlayers = pref.getString("numPlayers","Prompt");
		defaultWonderSide = pref.getString("wonderSide","Prompt");
		defaultWonder = pref.getString("wonder","Random");
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
		al.addView(wonderDescription);
	}

	public void displayResources(Player pp) {
		p=pp;
		final Activity cv = this;
		handler.post(new Runnable(){
			public void run() {
				for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
				for (View v:dualResourceIcons) v.setVisibility(View.VISIBLE);
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
						i1.setLayoutParams(new AbsoluteLayout.LayoutParams(60,30,0,topMargin+350+(resourceIcons.size()+dualResourceIcons.size()-24)*35));
						i1.setImageResource(vid);
						al.addView(i1);
						dualResourceIcons.add(i1);
					}
				}
				if (p.commerceDescription.length()>commerceString.length()) {
					Resources res = getResources();
					for (String s:p.commerceDescription.substring(commerceString.length(),p.commerceDescription.length()).split(",")) {
						commerceString+=s+",";
						int vid = res.getIdentifier(s.toLowerCase().replaceAll("/","")+"_resource", "drawable", getApplicationContext().getPackageName());
						ImageView i1 = new ImageView(cv);
						i1.setLayoutParams(new AbsoluteLayout.LayoutParams(60,30,0,topMargin+350+(resourceIcons.size()+dualResourceIcons.size()-24)*35));
						i1.setImageResource(vid);
						al.addView(i1);
						dualResourceIcons.add(i1);
					}
				}
			}
		});
	}

	public void displayNeighborResources(Player pp) {
		idText.setText("Player "+pp.id);
		if (pp.isWonderBSide) nameText.setText(pp.wonder.name+"(B)");
		else nameText.setText(pp.wonder.name+"(A)");
		coinText.setText(""+pp.numCoin);
		clayText.setText(""+pp.numClay);
		oreText.setText(""+pp.numOre);
		stoneText.setText(""+pp.numStone);
		woodText.setText(""+pp.numWood);
		glassText.setText(""+pp.numGlass);
		loomText.setText(""+pp.numLoom);
		papyrusText.setText(""+pp.numPapyrus);
		shieldText.setText(""+pp.numShield);
		wonderText.setText(""+pp.numWonderStages);
		for (View v:neighborDualResourceIcons) al.removeView(v);
		neighborDualResourceIcons=new ArrayList<View>();
		Resources res = getResources();
		for (String s:pp.resourceDescription.split(",")) {
			if (s.length()==0) break;
			int vid = res.getIdentifier(s.toLowerCase().replaceAll("/","")+"_resource", "drawable", getApplicationContext().getPackageName());
			ImageView i1 = new ImageView(this);
			i1.setLayoutParams(new AbsoluteLayout.LayoutParams(60,30,0,topMargin+350+(resourceIcons.size()+neighborDualResourceIcons.size()-24)*35));
			i1.setImageResource(vid);
			al.addView(i1);
			neighborDualResourceIcons.add(i1);
		}
		for (String s:pp.commerceDescription.split(",")) {
			if (s.length()==0) break;
			int vid = res.getIdentifier(s.toLowerCase().replaceAll("/","")+"_resource", "drawable", getApplicationContext().getPackageName());
			ImageView i1 = new ImageView(this);
			i1.setLayoutParams(new AbsoluteLayout.LayoutParams(60,30,0,topMargin+350+(resourceIcons.size()+neighborDualResourceIcons.size()-24)*35));
			i1.setImageResource(vid);
			al.addView(i1);
			neighborDualResourceIcons.add(i1);
		}
	}

	public void displayResourceIcon() {
		resourceIcons = new ArrayList<View>();
		dualResourceIcons = new ArrayList<View>();
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
	}

	public void displayMessageIcon() {
		messageIcons = new ArrayList<View>();
		messageText = new TextView(this);
		messageText.setTextColor(Color.BLACK);
		messageText.setLayoutParams(new AbsoluteLayout.LayoutParams(width-50,height/4*3,25,0));
		messageText.setGravity(Gravity.CENTER_VERTICAL);
		messageText.setTypeface(Typeface.MONOSPACE);
		al.addView(messageText);
		messageIcons.add(messageText);
		okButton = new Button(this);
		okButton.setText("OK");
		okButton.setLayoutParams(new AbsoluteLayout.LayoutParams(80,40,width/2-40,height/8*7-20));
		al.addView(okButton);
		messageIcons.add(okButton);
		for (View v:messageIcons) v.setVisibility(View.GONE);
	}

	public void displayNeighborIcon() {
		RadioButton[] rb = new RadioButton[numPlayers];
		radioIcons = new RadioGroup(this);
		radioIcons.setOrientation(RadioGroup.VERTICAL);
		radioIcons.setLayoutParams(new AbsoluteLayout.LayoutParams(width/4,height,width/4*3,topMargin));
		radioIcons.setVisibility(View.GONE);
		for(int i=0; i<numPlayers; i++){
			final int j=i;
			rb[i]  = new RadioButton(this);
			if (i==0) radioZero = rb[i];
			rb[i].setTextColor(Color.BLACK);
			rb[i].setText(""+i);
			rb[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Player cp;
					switch (j) {
						case 0:cp=p;break;
						case 1:cp=p.left;break;
						case 2:cp=p.left.left;break;
						case 3:cp=p.left.left.left;break;
						case 4:cp=p.left.left.left.left;break;
						case 5:cp=p.left.left.left.left.left;break;
						case 6:cp=p.left.left.left.left.left.left;break;
						default:cp=p;break;
					}
					displayNeighborResources(cp);
					displayCardsPlayed(cp.playedCards);
				}  
			});
			radioIcons.addView(rb[i]);
		}
		al.addView(radioIcons);
	}

	public void displayText(String s,OnClickListener listener) {
		final String ss = s;
		final OnClickListener l = listener;
		handler.post(new Runnable(){
			public void run() {
				messageText.setText(ss);
				okButton.setOnClickListener(l);
				for (View v:messageIcons) v.setVisibility(View.VISIBLE);
				for (View v:resourceIcons) v.setVisibility(View.GONE);
				for (View v:dualResourceIcons) v.setVisibility(View.GONE);
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
					int vid = res.getIdentifier(cards.get(i).name.toLowerCase().replaceAll(" ","_"), "drawable", getApplicationContext().getPackageName());
					iv.setImageResource(vid);
				}
				for (int i=0;i<numCards;i++) {
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

	public void displayCardsPlayed(ArrayList<Cards> c) {
		int cardWidth=width/2;
		int cardHeight=70;
		for (View v:neighborCardViews) al.removeView(v);
		neighborCardViews=new ArrayList<View>();
		int numCards=c.size();
		for (int i=0;i<numCards;i++) {
			final int j=i;
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(new AbsoluteLayout.LayoutParams(cardWidth,cardHeight,cardWidth/2,topMargin+cardHeight*i));
			al.addView(iv);
			neighborCardViews.add(iv);
			Resources res = getResources();
			int vid = res.getIdentifier(c.get(i).name.toLowerCase().replaceAll(" ","_"), "drawable", getApplicationContext().getPackageName());
			iv.setImageResource(vid);
		}
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
				for (View v:cardViews) al.removeView(v);
				for (View v:descriptionIcons) v.setVisibility(View.GONE);
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
//				for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
//				for (View v:dualResourceIcons) v.setVisibility(View.VISIBLE);
				for (int i=0;i<cards.size()*2;i++) cardViews.get(i).setVisibility(View.VISIBLE);
				al.removeView(cardDescription);
				displayResources(p);
			}
		});
		al.addView(backButton);
		descriptionIcons.add(backButton);
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
		cardDescription = new ImageView(this);
		cardDescription.setLayoutParams(new AbsoluteLayout.LayoutParams(width,height/2,0,0));
		al.addView(cardDescription);
		cardDescription.setImageResource(vid);
		String s = c.getDescription();
		playButton.setEnabled(false);
		if (playableCost[i]==-2) s+="\nCannot build 2 identical structures";
		else if (playableCost[i]==-1) s+="\nNot enough resources to build";
		else playButton.setEnabled(true);
		if (p.hasFreeBuild>0&&playableCost[i]!=-2) playButton.setEnabled(true);
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
		for (View v:dualResourceIcons) v.setVisibility(View.GONE);
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

	public void setWonder() {
		String s = p.wonder.name.toLowerCase()+"_";
		if (isWonderBSide) s+="b";
		else s+="a";
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

	public void displayScore(Player[] p,ArrayList<Integer> winner,ArrayList<Integer> totalScore,int[][] scoreCategories) {
		String s = "Score:\n";
		s+=" | BR GY  Y BL GN  R  P | V  D \n";
		for (Player pp:p) {
			s+=String.format("%1d| %2d %2d %2d %2d %2d %2d %2d | %2d %2d\n",pp.id,pp.numBrown,pp.numGray,pp.numYellow,pp.numBlue,pp.numGreen,pp.numRed,pp.numPurple,pp.victoryToken,pp.defeatToken);
		}
		s+=" |Mil Cn Won Civ Sci Com Gld|Sum\n";
		for (int i=0;i<p.length;i++) {
			s+=String.format("%1d|%3d %2d %3d %3d %3d %3d %3d|%3d\n",i,
				scoreCategories[0][i],
				scoreCategories[1][i],
				scoreCategories[2][i],
				scoreCategories[3][i],
				scoreCategories[4][i],
				scoreCategories[5][i],
				scoreCategories[6][i],
				totalScore.get(i));
		}
		if (winner.size()==1) s+="The winner is Player "+winner.get(0)+"!";
		else {
			s+="The winners are";
			for (Integer i:winner) {
				s+=" Player "+i;
			}
			s+="!";
		}
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
		eventInfo+=src+" discards for 3 coin\n";
	}

	public void showWonderAction(WonderStage w,int numWonderStages,String name){
		eventInfo+=name+" built Wonder Stage "+numWonderStages+" ";
		if (w.numCoin>0) {
			eventInfo+="+"+w.numCoin+" COIN ";
		}
		if (w.numShield>0) {
			eventInfo+="+"+w.numShield+" SHIELD ";
		}
		eventInfo+="\n";
	}

	public void showCardAction(String cardName,int dCoin,String playerName){
		eventInfo+=playerName+" played "+cardName;
		if (dCoin>0) eventInfo+=" for "+dCoin+" coin\n";
		else eventInfo+="\n";
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
//					for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
//					for (View v:dualResourceIcons) v.setVisibility(View.VISIBLE);
				}
				synchronized(lock) {
					lock.notify();
				}
			}
		});
	}

	public void selectWonderSide(Player pp){
		p=pp;
		if (defaultWonderSide.equals("Prompt")) {
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
		} else if (defaultWonderSide.equals("Random")) {
			p.isWonderBSide=random.nextInt(2)==1;
			isWonderBSide=p.isWonderBSide;
			setWonder();
		} else if (defaultWonderSide.equals("Side A")) {
			p.isWonderBSide=false;
			isWonderBSide=false;
			setWonder();
		} else if (defaultWonderSide.equals("Side B")) {
			p.isWonderBSide=true;
			isWonderBSide=true;
			setWonder();
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
		if (defaultNumPlayers.equals("Prompt")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder
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
		} else if (defaultNumPlayers.equals("Random")) {
			Controller.defaultNumPlayers=random.nextInt(5)+3;
			controllerThread.start();
		} else {
			try {Controller.defaultNumPlayers=Integer.parseInt(defaultNumPlayers);}
			catch (NumberFormatException e) {}
			controllerThread.start();
		}
		numPlayers=Controller.defaultNumPlayers;
		if (!defaultWonder.equals("Random")) Controller.defaultWonder=defaultWonder.toUpperCase();
		else Controller.defaultWonder=null;
		displayNeighborIcon();
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

	public void selectGuild(Player pp,ArrayList<Cards> guildChoices){
		p=pp;
		final ArrayList<Cards> options = guildChoices;
		final Activity cv = this;
		handler.post(new Runnable(){
			public void run() {
				CharSequence[] labels = new CharSequence[options.size()];
				for (int i=0;i<options.size();i++) labels[i]=options.get(i).name;
				AlertDialog.Builder builder = new AlertDialog.Builder(cv);
				builder
					.setTitle("Wonder effect: Select guild to copy from neighbors:")
					.setCancelable(false)
					.setItems(labels, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Cards c = options.get(id);
								p.playedCards.add(c);
								p.applyCardEffect(c);
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

	public void selectFromDiscard(Player pp,ArrayList<Cards> discardPile_, ArrayList<Cards> selection_){
		p=pp;
		final ArrayList<Cards> discardPile = discardPile_;
		final ArrayList<Cards> selection = new ArrayList<Cards>();
		for (Cards c:selection_) if (c!=null) selection.add(c);
		final Activity cv = this;
		handler.post(new Runnable(){
			public void run() {
				CharSequence[] labels = new CharSequence[selection.size()];
				for (int i=0;i<selection.size();i++) labels[i]=selection.get(i).name;
				AlertDialog.Builder builder = new AlertDialog.Builder(cv);
				builder
					.setTitle("Wonder effect: Select card to play from discard pile:")
					.setCancelable(false)
					.setItems(labels, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Cards c = selection.get(id);
								for (int i=0;i<discardPile.size();i++) {
									if (discardPile.get(i)==c) {
										p.playedCards.add(discardPile.remove(i));
										p.applyCardEffect(c);
									}
								}
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

	public void updateView() {
		displayText(eventInfo,new OnClickListener() {
			public void onClick(View arg0) {
//				if (currentTurn!=6) {
					for (View v:messageIcons) v.setVisibility(View.GONE);
//					for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
//					for (View v:dualResourceIcons) v.setVisibility(View.VISIBLE);
//				}
				synchronized(lock) {
					lock.notify();
				}
			}
		});
		eventInfo="";
	}

	public void selectAction(Player p,ArrayList<Cards> cards){
		synchronized(lock) {
			try {lock.wait();}
			catch (InterruptedException e) {}
		}
	} 

	public void displayWonders(Wonder[] w){}
	public void displayPlayerName(String s){}
	public void displayDiscardPile(ArrayList<Cards> discardPile){}
	public void displayNeighborResources(String name,Player p){}
	public void selectLookAction(Player p,ArrayList<Cards> cards){} 
	public void selectCard(Player p,ArrayList<Cards> cards){}

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }
	
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		case R.id.settingsmenu:
			startActivity(new Intent(this,SettingsActivity.class));
			return true;
		case R.id.cardsplayedmenu:
			previousIcons=new ArrayList<View>();
			if (resourceIcons.get(0).getVisibility()==View.VISIBLE) {
				for (View v:cardViews) {v.setVisibility(View.GONE); previousIcons.add(v);}
				for (View v:dualResourceIcons) {v.setVisibility(View.GONE); previousIcons.add(v);}
			} else if (descriptionIcons.get(0).getVisibility()==View.VISIBLE) {
				for (View v:resourceIcons) {v.setVisibility(View.VISIBLE);}
				for (View v:descriptionIcons) {v.setVisibility(View.GONE);previousIcons.add(v);}
				cardDescription.setVisibility(View.GONE);previousIcons.add(cardDescription);
			} else {
				for (View v:resourceIcons) {v.setVisibility(View.VISIBLE);}
				for (View v:messageIcons) {v.setVisibility(View.GONE);previousIcons.add(v);}
			}
			radioIcons.setVisibility(View.VISIBLE);
			radioZero.setChecked(true);
			displayCardsPlayed(p.playedCards);
			displayNeighborResources(p);
			return true;
		case R.id.wondermenu:
			if (wonderDescription.getVisibility()!=View.VISIBLE) {
				previousIcons=new ArrayList<View>();
				if (resourceIcons.get(0).getVisibility()==View.VISIBLE) {
					for (View v:resourceIcons) {v.setVisibility(View.GONE); previousIcons.add(v);}
					for (View v:dualResourceIcons) {v.setVisibility(View.GONE); previousIcons.add(v);}
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
	public void onBackPressed() {
		if (radioIcons.getVisibility()==View.VISIBLE) {
			radioIcons.setVisibility(View.GONE);
			for (View v:previousIcons) v.setVisibility(View.VISIBLE);
			for (View v:neighborCardViews) v.setVisibility(View.GONE);
			for (View v:neighborDualResourceIcons) v.setVisibility(View.GONE);
			if (cardDescriptionText.getVisibility()==View.VISIBLE||messageText.getVisibility()==View.VISIBLE) {
				for (View v:resourceIcons) v.setVisibility(View.GONE);
				for (View v:dualResourceIcons) v.setVisibility(View.GONE);
			} else displayResources(p);
		} else if (wonderDescription.getVisibility()==View.VISIBLE) {
			for (View v:previousIcons) v.setVisibility(View.VISIBLE);
			wonderDescription.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event ) {
		 if(keycode == KeyEvent.KEYCODE_MENU){
			if (radioIcons.getVisibility()==View.VISIBLE||wonderDescription.getVisibility()==View.VISIBLE) {
				return true;
			}
		 }
		 return super.onKeyDown(keycode,event);  
	}

}
