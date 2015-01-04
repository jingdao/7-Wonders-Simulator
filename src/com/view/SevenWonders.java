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
import android.graphics.Point;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.res.Resources;
import android.widget.AbsoluteLayout;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import model.Cards;
import model.Player;
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
	int currentCard;
	ArrayList<Cards> cards;
	int[] playableCost;
	Player p;
	ArrayList<View> resourceIcons;
	ArrayList<View> cardViews;
	ArrayList<View> descriptionIcons;
	ImageView cardDescription;
	TextView cardDescriptionText,ageText,turnText,idText,nameText;
	Button playButton;
	AbsoluteLayout al;
	int topMargin=30;
	String resourceString="",commerceString="";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight()-getStatusBarHeight();
		
		al = (AbsoluteLayout) findViewById(R.id.layout1);
//		displayCardIcon();
		displayResourceIcon();
		displayDescriptionIcons();
		Controller.manualSimulation=true;
		final CardView cv = this;
		Thread t = new Thread(new Runnable(){
			public void run() {
				con=new Controller(cv);
			}
		});
		t.start();
//		con.newGame(7);
    }


	public int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
		      result = getResources().getDimensionPixelSize(resourceId);
		  } 
		  return result;
	}

	public void displayResources(Player pp) {
		p=pp;
		final Activity cv = this;
		handler.post(new Runnable(){
			public void run() {
				idText.setText("Player "+p.id);
				nameText.setText(p.wonder.name);
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
		idText.setLayoutParams(new AbsoluteLayout.LayoutParams(width/4,topMargin,0,0));
		idText.setGravity(Gravity.CENTER);
		al.addView(idText);
		nameText = new TextView(this);
		nameText.setTextColor(Color.BLACK);
		nameText.setLayoutParams(new AbsoluteLayout.LayoutParams(width/4,topMargin,width/4,0));
		nameText.setGravity(Gravity.CENTER);
		al.addView(nameText);
		ageText = new TextView(this);
		ageText.setTextColor(Color.BLACK);
		ageText.setLayoutParams(new AbsoluteLayout.LayoutParams(width/4,topMargin,width/2,0));
		ageText.setGravity(Gravity.CENTER);
		al.addView(ageText);
		turnText = new TextView(this);
		turnText.setTextColor(Color.BLACK);
		turnText.setLayoutParams(new AbsoluteLayout.LayoutParams(width/4,topMargin,width/4*3,0));
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
					if (playableCost[i]!=0) {
//						TextView tv = (TextView) cardViews.get(i+7);
						TextView tv = new TextView(cv);
						if (playableCost[i]<0) tv.setText("x");
						else if (playableCost[i]>0) tv.setText(""+playableCost[i]);
						tv.setTextColor(Color.BLACK);
						tv.setLayoutParams(new AbsoluteLayout.LayoutParams(30,cardHeight,width/4*3,topMargin+cardHeight*i));
						tv.setGravity(Gravity.CENTER);
//						tv.setVisibility(View.VISIBLE);
						al.addView(tv);
						cardViews.add(tv);
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
				p.lastCard=cards.remove(currentCard);
//				for (View v:cardViews) v.setVisibility(View.GONE);
				for (View v:cardViews) al.removeView(v);
				for (View v:descriptionIcons) v.setVisibility(View.GONE);
				for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
				al.removeView(cardDescription);
				synchronized(lock) {
					lock.notify();
				}
			}
		});
		al.addView(playButton);
		descriptionIcons.add(playButton);
		Button wonderButton = new Button(this);
		wonderButton.setText("Wonder");
		wonderButton.setLayoutParams(new AbsoluteLayout.LayoutParams(80,50,width/8*3-40,height/8*7-25));
		wonderButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				for (View v:descriptionIcons) v.setVisibility(View.GONE);
				for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
				for (int i=0;i<cards.size();i++) cardViews.get(i).setVisibility(View.VISIBLE);
				al.removeView(cardDescription);
			}
		});
		al.addView(wonderButton);
		descriptionIcons.add(wonderButton);
		Button discardButton = new Button(this);
		discardButton.setText("Discard");
		discardButton.setLayoutParams(new AbsoluteLayout.LayoutParams(80,50,width/8*5-40,height/8*7-25));
		discardButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				for (View v:descriptionIcons) v.setVisibility(View.GONE);
				for (View v:resourceIcons) v.setVisibility(View.VISIBLE);
				for (int i=0;i<cards.size();i++) cardViews.get(i).setVisibility(View.VISIBLE);
				al.removeView(cardDescription);
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
				for (int i=0;i<cards.size();i++) cardViews.get(i).setVisibility(View.VISIBLE);
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
		if (playableCost[i]==-2) s+="\n\nYou cannot build 2 identical structures";
		else if (playableCost[i]==-1) s+="\n\nYou do not have enough resources";
		else playButton.setEnabled(true);
		cardDescriptionText.setText(s);
		for (View v:cardViews) v.setVisibility(View.GONE);
		for (View v:resourceIcons) v.setVisibility(View.GONE);
		for (View v:descriptionIcons) v.setVisibility(View.VISIBLE);
	}

	public void message(String s){
		System.out.println(s);
	}

	public void displayAge(int age_){
		final int age = age_;
		handler.post(new Runnable(){
			public void run() {
				ageText.setText("Age "+age);
			}
		});
	}

	public void displayTurn(int turn_){
		final int turn = turn_;
		handler.post(new Runnable(){
			public void run() {
				turnText.setText("Turn "+turn);
			}
		});
	}

	public void displayScore(Player[] p,ArrayList<Integer> winner,ArrayList<Integer> totalScore,int[][] scoreCategories){
		String s = "";
		s+=" | BR GY Y  BL GN R  P  | V  D \n";
		for (Player pp:p) {
			s+=String.format("%1d| %2d %2d %2d %2d %2d %2d %2d| %2d %2d\n",pp.id,pp.numBrown,pp.numGray,pp.numYellow,pp.numBlue,pp.numGreen,pp.numRed,pp.numPurple,pp.victoryToken,pp.defeatToken);
		}
		s+=" | Mil Cn Won Civ Sci Com Gld | Sum\n";
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
		if (winner.size()==1) s+="The winner is Player "+winner.get(0)+"!\n";
		else {
			s+="The winners are";
			for (Integer i:winner) {
				s+=" Player "+i;
			}
			s+="!\n";
		}
		final Activity c = this;
		final String ss = s;
		handler.post(new Runnable(){
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
				builder.setMessage(ss)
//       			.setCancelable(false)
	          		.setPositiveButton("OK",null);
//	          		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//				             public void onClick(DialogInterface dialog, int id) {
//								                 //do things
//												            }
//															       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	public void selectAction(Player p,ArrayList<Cards> cards){
		System.out.println("get action");
		synchronized(lock) {
			try {lock.wait();}
			catch (InterruptedException e) {}
		}
	} 

	public void displayWonders(Wonder[] w){}
	public void displayPlayerName(String s){}
	public void displayDiscardPile(ArrayList<Cards> discardPile){}
	public void displayWarResults(Player[] p,int[] warResult){}
	public void displayPayment(String src,String dest,int amount){}
	public void displayNeighborResources(String name,Player p){}
	public void showDiscardAction(String src){}
	public void showWonderAction(WonderStage w,int numWonderStages,String name){}
	public void showCardAction(String cardName,int dCoin,String playerName){} 
	public void selectWonderSide(Player p){}
	public void selectFromDiscard(Player p,ArrayList<Cards> discardPile, ArrayList<Cards> selection){}
	public void selectGuild(Player p,ArrayList<Cards> guildChoices){}
	public void selectLookAction(Player p,ArrayList<Cards> cards){} 
	public void selectTrading(Player p, ArrayList<Integer> options){} 
	public void selectCard(Player p,ArrayList<Cards> cards){}

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }
	
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
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
}
