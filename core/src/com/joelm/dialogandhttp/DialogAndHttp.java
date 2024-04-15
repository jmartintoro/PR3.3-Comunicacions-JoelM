package com.joelm.dialogandhttp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.graphics.GL20;

public class DialogAndHttp extends ApplicationAdapter {
	Dialog endDialog;

	Skin skin;
	Stage stage;
	HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
	Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("https://www.google.com").build();
	Net.HttpResponseListener listener = new Net.HttpResponseListener() {
		@Override
		public void handleHttpResponse(Net.HttpResponse httpResponse) {
			System.out.println("OK: " + httpResponse.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {
			System.out.println("ERROR: " + t.toString());
		}

		@Override
		public void cancelled() {
			System.out.println("Canceled");
		}
	};

	@Override
	public void create()
	{
		skin = new Skin(Gdx.files.internal("freezing-ui.json"));

		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		endDialog = new Dialog("End Game", skin)
		{
			protected void result(Object object)
			{
				System.out.println("Option: " + object);
				if (object.equals(1L)) {
					System.out.println("Http");
					Gdx.net.sendHttpRequest(httpRequest, listener);
				} else {
					System.out.println("No http");
				}
				Timer.schedule(new Timer.Task()
				{

					@Override
					public void run()
					{
						endDialog.show(stage);
					}
				}, 1);
			};
		};

		endDialog.button("Option 1", 1L);
		endDialog.button("Option 2", 2L);

		Timer.schedule(new Timer.Task()
		{

			@Override
			public void run()
			{
				endDialog.show(stage);
			}
		}, 1);

	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();

	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}
}