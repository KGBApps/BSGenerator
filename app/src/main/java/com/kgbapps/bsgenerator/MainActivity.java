package com.kgbapps.bsgenerator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kgbapps.bsgenerator.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements HistoryListFragment.FragmentCloseCallBack, HistoryListFragment.GetHistoryCallBack, HistoryListFragment.PhraseClickCallBack {

    public static final String TAG = MainActivity.class.getSimpleName();

    private TextView mPhraseTextView;
    private TextView mBasedOnTextView;
    private TextView mIntroTextView;
    private Button mGenerateButton;
    private Button mResetButton;
    private Button mTweetButton;
    private Button mHistoryButton;
    private RelativeLayout mRelativeLayout;
    private WordBook mWordBook;
    private Generator mGenerator;
    private Phrase mPhrase;
    private History mHistory;
    private boolean hasPhrase;

    private ShareDialog mShareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mShareDialog = new ShareDialog(this);

        // Set Views
        mPhraseTextView = (TextView) findViewById(R.id.phraseTextView);
        mBasedOnTextView = (TextView) findViewById(R.id.basedOnTextView);
        mGenerateButton = (Button) findViewById(R.id.generateButton);
//        mResetButton = (Button) findViewById(R.id.resetButton);
        mTweetButton = (Button) findViewById(R.id.tweetButton);
        mHistoryButton = (Button) findViewById(R.id.historyButton);
        mIntroTextView = (TextView) findViewById(R.id.textView2);

        mWordBook = new WordBook(this);
        mGenerator = new Generator(mWordBook);
        mHistory = new History();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        // Reset to default view
//        resetPhrase();
        mPhrase = new Phrase("We need to", mGenerator.generatePhrase());
        handleNewPhrase(mPhrase);

//        mResetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetPhrase();
//            }
//        });

        mGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mTweetButton.setVisibility(View.VISIBLE);
                mPhrase = new Phrase("We need to", mGenerator.generatePhrase());
                handleNewPhrase(mPhrase);
            }
        });

        /*mTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! hasPhrase) {
                    Toast.makeText(MainActivity.this, "Please generate a phrase first", Toast.LENGTH_SHORT).show();
                } else {
                    // Transition this to being done with App Cards
                    TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this)
                            .text("\"We need to " + mPhrase + "\" - Created with the BS Generator App.");
                    builder.show();
                }
            }
        });*/

        mBasedOnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.atrixnet.com/bs-generator.html");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, mHistory.getHistory().getFirst().getLongPhrase(), Toast.LENGTH_LONG).show();
                HistoryListFragment fragment = new HistoryListFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_up, R.anim.slide_out_down)
                        .addToBackStack("HistoryList")
                        .add(R.id.fragmentContainer, fragment, "HistoryList")
                        .commit();
                setButtonsClickable(false);
            }
        });

        mRelativeLayout = (RelativeLayout) findViewById(R.id.clickContainer);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentClosed();
            }
        });
        mRelativeLayout.setClickable(false);
    }

    private void handleNewPhrase(Phrase phrase) {
        mHistory.addPhrase(phrase);
        setPhraseTextView(phrase);
    }

    private void removeFragment(android.support.v4.app.Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_up, R.anim.slide_out_down)
                    .remove(fragment)
                    .commit();
        }
    }

    private void setButtonsClickable(boolean b) {
        mHistoryButton.setClickable(b);
        mGenerateButton.setClickable(b);
        mBasedOnTextView.setClickable(b);
        mTweetButton.setClickable(b);
        if (b) {
            mRelativeLayout.setClickable(false);
        } else {
            mRelativeLayout.setClickable(true);
        }
    }

    private void resetPhrase() {
        setPhraseTextView(
                Constants.UNDERLINES,
                Constants.UNDERLINES,
                Constants.UNDERLINES,
                Constants.UNDERLINES
        );
//        mTweetButton.setVisibility(View.INVISIBLE);
        hasPhrase = false;
    }

    private void setPhraseTextView(String adverb, String verb, String adjective, String noun) {
        hasPhrase = true;
//        mPhrase = String.format("%s %s %s %s.",
//                adverb,
//                verb,
//                adjective,
//                noun
//        );
        mPhraseTextView.setText(String.format("%s %s %s %s.",
                adverb,
                verb,
                adjective,
                noun
        ));
    }

    private void setPhraseTextView(Phrase phrase) {
        hasPhrase = true;
        mPhraseTextView.setText(phrase.toString());
    }


    public void showSharePopup(View v) {
        if (!hasPhrase) {
            Toast.makeText(MainActivity.this, "Please generate a phrase first", Toast.LENGTH_SHORT).show();
        } else {
            PopupMenu popupMenu = new PopupMenu(this, v);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.share_twitter:
                            URL playStoreUrl = null;
                            try {
                                playStoreUrl = new URL(getString(R.string.play_store_link));
                                // Transition this to being done with App Cards
                                TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this)
                                        .text("\"We need to " + mPhrase + "\" @kgb_apps")
                                        .url(playStoreUrl);
                                builder.show();
                            } catch (MalformedURLException | NullPointerException e) {
                                Toast.makeText(MainActivity.this, "There was an error creating the Tweet", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                            return true;
                        case R.id.share_facebook:
                            ShareLinkContent content = new ShareLinkContent.Builder()
                                    .setContentUrl(Uri.parse(getString(R.string.play_store_link)))
                                    .setQuote("\"We need to " + mPhrase + "\"")
                                    .setContentTitle("Created with the BS Generator app")
                                    .setContentDescription("Need a quick BS phrase for your next corporate meeting or email? Quickly generate it with BS generator! BS generator makes it easy to create and share buzz-wordy phrases.")
                                    .build();
                            mShareDialog.show(content);
                            return true;
                        case R.id.share_clipboard:
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("BS Phrase", "\"We need to " + mPhrase + "\"");
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(MainActivity.this, "Phrase copied to clipboard", Toast.LENGTH_SHORT).show();

                            return true;
                        default:
                            return false;
                    }
                }
            });
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.share_menu, popupMenu.getMenu());
            popupMenu.show();
        }
    }

    @Override
    public void fragmentClosed() {
        setButtonsClickable(true);
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag("HistoryList");
        removeFragment(fragment);
    }

    @Override
    public History getHistory() {
        return mHistory;
    }

    @Override
    public void phraseClick(Phrase phrase) {
        mHistory.removePhrase(phrase);
        mPhrase = phrase;
        handleNewPhrase(mPhrase);
        fragmentClosed();
    }
}
