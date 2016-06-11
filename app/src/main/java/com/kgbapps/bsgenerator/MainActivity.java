package com.kgbapps.bsgenerator;

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

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private TextView mPhraseTextView;
    private TextView mBasedOnTextView;
    private Button mGenerateButton;
    private Button mResetButton;
    private Button mTweetButton;
    private WordBook mWordBook;
    private String mPhrase;
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

        mWordBook = new WordBook(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        // Reset to default view
        resetPhrase();

        mPhrase = "";

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
                setPhraseTextView(
                        mWordBook.getRandomAdverb(),
                        mWordBook.getRandomVerb(),
                        mWordBook.getRandomAdjective(),
                        mWordBook.getRandomNoun()
                );
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
        mPhrase = String.format("%s %s %s %s.",
                adverb,
                verb,
                adjective,
                noun
        );
        mPhraseTextView.setText(String.format("%s %s %s %s.",
                adverb,
                verb,
                adjective,
                noun
        ));
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
                            } catch (MalformedURLException e) {
                                Toast.makeText(MainActivity.this, "There was an error creating the Tweet", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            // Transition this to being done with App Cards
                            TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this)
                                    .text("\"We need to " + mPhrase + "\" - Created with the BS Generator App.")
                                    .url(playStoreUrl);
                            builder.show();

                            return true;
                        case R.id.share_facebook:
                            ShareLinkContent content = new ShareLinkContent.Builder()
                                    .setContentUrl(Uri.parse(getString(R.string.play_store_link)))
                                    .setQuote("\"We need to " + mPhrase + "\"")
                                    .setContentTitle("Created with the BS Generator app")
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

}
