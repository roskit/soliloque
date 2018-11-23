//--- Soliloque SampleView ----------------------------
//--- (c) 2002,2006 by Thomas Seelig and Frederic Roskam
//--- seelig@snafu.de ---------------------------------
//--- froskam@gmail.com--------------------------------

SampleView {
		// Instance variables: var a, <b, >c, <>d; a has no getter or setter.
		// => b has a getter but not a setter. c has only a setter. d has both a getter and setter.
		var <version = 7.43;

		var	<>tmax = 0, 		// position of the max in the 1st part (in Samples !)
			<>vmax = 0,
			<>fmax = 0,
			<>tmx = 0,			// position of the max in the 2nd part (in Samples !)
			<>fmx = 0,
			<>vmx = 0,
			<>tmin = 0,			// position of the min (in Samples !)
			<>vmin = 0,
			<>fmin = 0,
			<>len = 0;			// Length in seconds

		var	<>itsSCSoundFileView, 	// View of the sound file
			<>itsLoadButton,		// Load button
			<>itsPrepareButton,		// Make button
			<>itsPlaybackButton, 	// Play button
			<>itsAnalyzeButton,	// Analyse button
			<>itsSaveButton,
			<>itsSetTmax1Button, 	// Set Tmax button
			<>itsSetTmax2Button, 	// Set Tmx button
			<>itsSetTminButton, 	// Set Tmin button
			<>itsRefreshButton, 	// Normalize button
			<>itsCropButton, 		// Crop button
			<>itsTmax1Num = 0, 		// Tmax
			<>itsFmax1Num = 0, 		// Fmax
			<>itsVmax1Num = 0, 		// Vmax
			<>itsTmax2Num = 0, 		// Tmx
			<>itsFmax2Num = 0, 		// Fmx
			<>itsVmax2Num = 0, 		// Vmx
			<>itsTminNum = 0, 		// Tmin
			<>itsFminNum = 0, 		// Fmin
			<>itsVminNum = 0,		// Vmin
			<>itsLenNum = 0,		// LenNum

			<>itsSoundFileName,	// Sound file name
			<>itsSoundFile, 		// Sound file
			<>itsSignal, 			// Sound (signal itself)
			<>itsAbsSignal,		// Absolute version of the signal
			<>itsPitchFile, 		// Pitch file
			<>itsPitchFileName,	// Pitch file name
			<>itsPitchBuffer,		// Pitch buffer
			<>itsPitch,			// Pitch
			<>itsConfigFileName,	// Config file name
			<>itsSR,				// Server sample rate (Hz)
			<>itsBuffer = nil;

	//---------- new --------------------------------------------------------------

	*new{arg	aWindow,
			aXOffset = 0,
			aYOffset = 0,
			aLabel = "Sample x",
			aSoundFileName;

		^super.new.init(aWindow, aXOffset, aYOffset, aLabel, aSoundFileName);	}	// new


	//---------- init --------------------------------------------------------------

	init {arg	aWindow,
			aXOffset,
			aYOffset,
			aLabel,
			aSoundFileName;

		var s,a;

		s = Server.default;
		this.itsSR = s.sampleRate;
		("INFO: Initialize " ++ aSoundFileName).postln;

		// Create buttons and text for the current sample
		this.createViews(aWindow, aLabel, aXOffset, aYOffset);

		// Define actions (functions) for each button
		this.itsLoadButton.action = 	{this.loadProc};
		this.itsPrepareButton.action = 	{this.prepareProc};
		this.itsPlaybackButton.action = {this.playProc};
		this.itsAnalyzeButton.action = 	{this.analyzeProc};
		this.itsSaveButton.action = 	{this.saveProc};
		this.itsSetTmax1Button.action = {this.setTmax1Proc};
		this.itsSetTmax2Button.action = {this.setTmax2Proc};
		this.itsSetTminButton.action = 	{this.setTminProc};
		this.itsRefreshButton.action =	{this.updateView};
		
		// Check for the presence of default files
		this.itsSoundFileName = aSoundFileName;
		this.itsPitchFileName = this.itsSoundFileName ++ ".pitch".asString;
		this.itsConfigFileName = this.itsSoundFileName ++ ".cfg".asString;
		
		//  open existing sound files
		this.itsBuffer = Buffer.read(s, this.itsSoundFileName,
			action: {
				postf("INFO: % loaded\n",this.itsSoundFileName);

				//  open existing pitch files
				this.itsPitchBuffer = Buffer.read(s, this.itsPitchFileName,action: {
					postf("INFO: % loaded\n",this.itsPitchFileName);
					this.getSignal(nil);
					postf("INFO: % -> Array\n",this.itsSoundFileName);
					this.getPitch(nil);
					postf("INFO: % -> Array\n",this.itsPitchFileName);

					//  open existing config file
					this.readConfigFile(nil);
				});
			});
		
		// Update the interface after 2 seconds
		{this.updateView(nil);}.defer(2.0);
		
	}	// init


	//----- createViews ----------------------------
	createViews {
		arg aWindow, aLabel, aXOffset, aYOffset;

		// Wave form window (and options)
		this.itsSCSoundFileView 				= SCSoundFileView.new( aWindow, Rect(10 + aXOffset, 31 + aYOffset, 197, 112));
		this.itsSCSoundFileView.timeCursorOn 		= true;
		this.itsSCSoundFileView.timeCursorColor 	= Color.red;
		this.itsSCSoundFileView.timeCursorPosition= 0;
		this.itsSCSoundFileView.drawsWaveForm 	= true;
		this.itsSCSoundFileView.gridOn 			= true;
		this.itsSCSoundFileView.gridResolution 	= 1.0; // 1 second
		// text : sample #
		SCStaticText( aWindow, Rect.new(7 + aXOffset, 8 + aYOffset, 128, 20)).string = aLabel;
		//--- Values for Tmax, Tmx ... --------------
		this.itsTmax1Num		= SCNumberBox( aWindow, Rect.new(210 + aXOffset, 31 + aYOffset, 45, 14));
		this.itsFmax1Num 		= SCNumberBox( aWindow, Rect.new(210 + aXOffset, 49 + aYOffset, 45, 14));
		this.itsVmax1Num 		= SCNumberBox( aWindow, Rect.new(210 + aXOffset, 67 + aYOffset, 45, 14));
		this.itsTmax2Num 		= SCNumberBox( aWindow, Rect.new(310 + aXOffset, 31 + aYOffset, 45, 14));
		this.itsFmax2Num 		= SCNumberBox( aWindow, Rect.new(310 + aXOffset, 49 + aYOffset, 45, 14));
		this.itsVmax2Num 		= SCNumberBox( aWindow, Rect.new(310 + aXOffset, 67 + aYOffset, 45, 14));
		this.itsTminNum 		= SCNumberBox( aWindow, Rect.new(410 + aXOffset, 31 + aYOffset, 45, 14));
		this.itsFminNum 		= SCNumberBox( aWindow, Rect.new(410 + aXOffset, 49 + aYOffset, 45, 14));
		this.itsVminNum 		= SCNumberBox( aWindow, Rect.new(410 + aXOffset, 67 + aYOffset, 45, 14));
		this.itsLenNum 		= SCNumberBox( aWindow, Rect.new(210 + aXOffset, 85 + aYOffset, 45, 14));
		//--- Buttons with authorized states -----------------------
		this.itsLoadButton			= SCButton( aWindow, Rect.new(145 + aXOffset, 10 + aYOffset, 60, 18));
		this.itsLoadButton.states 	= [["load",Color.black,Color.new255(249,182,172)]];

		this.itsPlaybackButton 		= SCButton( aWindow, Rect.new(210 + aXOffset, 10 + aYOffset, 60, 18));
		this.itsPlaybackButton.states = [["play",Color.black,Color.new255(177,237,105)]];

		this.itsPrepareButton		= SCButton( aWindow, Rect.new(275 + aXOffset, 10 + aYOffset, 70, 18));
		this.itsPrepareButton.states= [["prepare",Color.white,Color.black]];

		this.itsAnalyzeButton 		= SCButton( aWindow, Rect.new(350 + aXOffset, 10 + aYOffset, 80, 18));
		this.itsAnalyzeButton.states = [["auto-analyse",Color.white,Color.black],["busy...",Color.red,Color.black],["",Color.gray,Color.gray]];

		this.itsSaveButton          = SCButton( aWindow, Rect.new(435 + aXOffset, 10 + aYOffset, 55, 18));
		this.itsSaveButton.states = [["save",Color.white,Color.new255(231,160,40)],["save",Color.white,Color.new255(231,160,40)]];

		this.itsSetTmax1Button 		= SCButton( aWindow, Rect.new(210 + aXOffset, 104 + aYOffset, 80, 18));
		this.itsSetTmax1Button.states = [["set tmax1",Color.white,Color.black]];
		this.itsSetTmax2Button 		= SCButton( aWindow, Rect.new(310 + aXOffset, 104 + aYOffset, 80, 18));
		this.itsSetTmax2Button.states = [["set tmax2",Color.white,Color.black]];
		this.itsSetTminButton 		= SCButton( aWindow, Rect.new(410 + aXOffset, 104 + aYOffset, 80, 18));
		this.itsSetTminButton.states = [["set tmin",Color.white,Color.black]];

		this.itsRefreshButton	= SCButton( aWindow, Rect.new(210 + aXOffset, 125 + aYOffset, 280, 18));
		this.itsRefreshButton.states = [["refresh window",Color.white,Color.black]];
		//--- Extra text ------------------
		SCStaticText( aWindow, Rect.new(260 + aXOffset, 31 + aYOffset, 47, 14)).string = "tmax1";
		SCStaticText( aWindow, Rect.new(260 + aXOffset, 49 + aYOffset, 47, 14)).string = "freq";
		SCStaticText( aWindow, Rect.new(260 + aXOffset, 67 + aYOffset, 47, 14)).string = "amp";
		SCStaticText( aWindow, Rect.new(360 + aXOffset, 31 + aYOffset, 47, 14)).string = "tmax2";
		SCStaticText( aWindow, Rect.new(360 + aXOffset, 49 + aYOffset, 47, 14)).string = "freq";
		SCStaticText( aWindow, Rect.new(360 + aXOffset, 67 + aYOffset, 47, 14)).string = "amp";
		SCStaticText( aWindow, Rect.new(460 + aXOffset, 31 + aYOffset, 47, 14)).string = "tmin";
		SCStaticText( aWindow, Rect.new(460 + aXOffset, 49 + aYOffset, 47, 14)).string = "freq";
		SCStaticText( aWindow, Rect.new(460 + aXOffset, 67 + aYOffset, 47, 14)).string = "amp";
		SCStaticText( aWindow, Rect.new(260 + aXOffset, 85 + aYOffset, 47, 14)).string = "seconds";

	}	// end createViews


	//--- loadProc : load a new sound file ---------------------------------------------------------------
	loadProc {
		var newSoundFile;	// Sound file to create
		var s;

		s = Server.default;

		CocoaDialog.getPaths(
			// If correct file name returned
			{
				arg inputSoundFileName;

				var return;
				var sampleRate; 	// Sample rate
				var signalLength; 	// number of frames

				// trace the name of the file
				//inputSoundFileName[0].postln;

				newSoundFile = SoundFile.new;

				// Open file (read header only)
				return = newSoundFile.openRead(inputSoundFileName[0]);

				if (return == true,
					{
						// Display file length in seconds
						("INFO: Sound file length (in sec): " ++ newSoundFile.duration).postln;
						
						// Try to delete the existing sound files
						File.delete(this.itsSoundFileName);
						File.delete(this.itsPitchFileName);
						//File.delete(this.itsConfigFileName);
						// Reset config for this sample
						this.resetConfig(nil);

						// Get sound file sample rate
						sampleRate = newSoundFile.sampleRate;
						("INFO: Sound file sample rate:" ++ sampleRate ++ " Hz" ).postln;

						// Limit sound length to 60 seconds
						if (
							newSoundFile.duration > 60,
							// then
							{signalLength = sampleRate * 60; "WARNING: max file length is 60s".postln;},
							// else
							{signalLength = newSoundFile.numFrames;}
						);

						// Update the length value on the window
						this.itsLenNum.value = signalLength/sampleRate;

						// Copy sound file in the buffer
						"INFO: if sound file is stereo, left channel is only kept";
						this.itsBuffer = Buffer.readChannel(s,path:newSoundFile.path,numFrames:signalLength,channels: [0]);

						// Update the signal view window
						this.itsSCSoundFileView.soundfile = newSoundFile;
						this.itsSCSoundFileView.read(0, signalLength, 64, true); //startframe, frames, block, closeFile
						this.itsSCSoundFileView.refresh;

						// Copy sound file to disk
						this.itsBuffer.write(this.itsSoundFileName,"wav","int16",
							completionMessage: { 
								postf("INFO: % written to the disk\n",this.itsSoundFileName);
								postf("INFO: Please click on the [ANALYSE] button for this sample\n");
							},
							leaveOpen:false
						);
						this.itsAnalyzeButton.value = 2;

					},
					{
						"ERROR: problem reading the sound file".postln;
						return.postln;
					}
				); // end if


			},
			// if invalid file selection
			{
				"WARNING: no sound file selected".postln;
			},	
			// max number of files allowed to be selected
			1
			); // end getPath


	}	// loadProc


	//--- playProc ---------------------------------------------------------------

	playProc {
		var transp = 1, size, start, end;
		var s, b, return, nextSynth,x;

		s = Server.default;

		// If no Synth is playing
		if (s.numSynths == 0,
			{
				// Play
				postf("INFO: Play %\n",this.itsSoundFileName);
				// Get start frame and end frame
				start = this.itsSCSoundFileView.selectionStart(0);
				size  = this.itsSCSoundFileView.selectionSize(0);

				if (size>0,
					{
						// Play selection
						SynthDef("playfile",{
							arg	out = 0,
								start = 0,
								tscale = 1;

							Out.ar(out,
								EnvGen.ar(
									Env.new(#[1, 1], #[1.0]),
									gate:1.0,   // loop
									timeScale:tscale,
									doneAction: 2)*
								PlayBuf.ar(
									1,
									this.itsBuffer.bufnum,
									//trigger:0,
									rate:BufRateScale.kr(this.itsBuffer.bufnum),
									startPos:start)
							);
						}).play(s,[\start,start,\tscale,size/this.itsBuffer.sampleRate]);

						postf("INFO: Play selection (% sec)\n",size/this.itsBuffer.sampleRate);
						
					},
					{
						"INFO: Play whole file".postln;
						this.itsBuffer.play;
					}
				);
			},
			{
				// Stop
				"INFO: Stop".postln;
				Server.freeAll;
			}
		); // end if
	}	// playProc

	//--- prepareProc = prep button ---------------------------------------------------------------

	prepareProc{
		var soundLength; // sound file length in seconds
		var s, node;

		this.itsAnalyzeButton.value = 2;

		s = Server.default;

		// Beginning of a Routine
		{
			soundLength = this.itsBuffer.numFrames/this.itsBuffer.sampleRate;
			this.itsPitchBuffer = Buffer.alloc(s,this.itsBuffer.numFrames);
			SynthDef("PitchFollow",{arg buffin = 0,buffout = 1;
				var out = 0;
				var x, freq;

				// Save frequencies into the output buffer
				freq = K2A.ar(Pitch.kr(PlayBuf.ar(1, buffin, BufRateScale.kr(buffin)),median:1,minFreq:20,maxFreq:800)).at(0);
				x = RecordBuf.ar(freq, buffout);
				// Optionnal
				Out.ar( out, PlayBuf.ar(1, buffin, BufRateScale.kr(buffin)));
			}).send(s);
			// Wait for the Synth Def to be transmitted
			2.wait;

			// Run Synth
			postf("INFO: Prepare %\n",this.itsSoundFileName);
			node = Synth(\PitchFollow, [\buffin,this.itsBuffer.bufnum,\buffout,this.itsPitchBuffer.bufnum]);
			soundLength.wait;

			// Save pitch buffer into a file
			this.itsPitchBuffer.write(this.itsPitchFileName, headerFormat: 'Sun', sampleFormat:'float32');

			postf("INFO: % prepared. Please click on [Analyse] \n",this.itsSoundFileName);
			node.free;
			
		}.fork;
		soundLength = this.itsBuffer.numFrames/this.itsBuffer.sampleRate;
		{this.itsAnalyzeButton.value = 0;}.defer(soundLength+2);
		
	}	// prepareProc

	//--- analyzeProc ---------------------------------

	analyzeProc {
		var tsec, i, t, v, f;
		var d, s, return;

		this.itsAnalyzeButton.value = 1;

		s = Server.default;

		// Copy the sound file into a local array
		this.getSignal(nil);
		// Copy the pitch file into a local array
		this.getPitch(nil);

		// tmax (samples) = maximum amplitude position of the signal in the 1st half
		tmax					= this.findtmax1Func(this.itsAbsSignal);
		tmax.postln;
		this.itsTmax1Num.value 	= tmax / this.itsSR;
		// vmax = amplitude at tmax
		this.itsVmax1Num.value 	= vmax = (this.itsAbsSignal.at(tmax));
		// fmax = frequency at tmax
		this.itsFmax1Num.value 	= fmax = (this.itsPitch.at(tmax));


		// tmx (samples) = maximum amplitude position of the signal in the 2nd half
		tmx 					= this.findtmax2Func.value(this.itsAbsSignal);
		tsec 					= tmx / this.itsSR;
		this.itsTmax2Num.value 	= tsec;
		this.itsVmax2Num.value 	= vmx = this.itsAbsSignal.at(tmx);
		this.itsFmax2Num.value 	= fmx = this.itsPitch.at(tmx);

		// tmin (samples) = minimum amplitude position of the signal between the tmax and tmx
		tmin 				= this.findtminFunc.value(this.itsAbsSignal, tmx, tmax);
		tsec 				= tmin / this.itsSR;
		this.itsTminNum.value 	= tsec;
		this.itsVminNum.value 	= vmin = this.itsAbsSignal.at(tmin);
		this.itsFminNum.value 	= fmin = this.itsPitch.at(tmin);
		len 					= (this.itsSignal.size - 2) / this.itsSR;

		this.itsLenNum.value = len;

		this.itsAnalyzeButton.value = 0;

		// Save values
		this.writeConfigFile(nil);
		postf("INFO: Analysis done. You may want to tweak the automatic results by using the [set tmax1], [set tmax2] and [set tmin] buttons\n",this.itsSoundFileName);

	}	// analyzeProc


	//--- saveProc ------------------------------------

	saveProc {
		// Save values
		this.writeConfigFile(nil);
		this.updateView(nil);
		"INFO: Configuration saved".postln;
	}	// saveProc

	//--- setTmax1Proc --------------------------------
	setTmax1Proc {

		tmax = this.itsSCSoundFileView.selectionStart(0);
		("INFO: frame selected: " ++ tmax).postln;

		if (this.itsSignal.isNil,
			{
				"ERROR: Please re-prepare and analyse first".postln;
			},
			{
				this.itsTmax1Num.value 	= tmax / this.itsSR;
				this.itsVmax1Num.value 	= vmax = this.itsSignal[tmax].abs;
				this.itsFmax1Num.value 	= fmax = this.itsPitch[tmax];
				this.itsSCSoundFileView.timeCursorPosition = tmax;
			}
		);
		
		this.itsSaveButton.value = 1;

	}	// setTmax1Proc

	//--- setTmax2Proc ---------------------------------
	setTmax2Proc {
		tmx = this.itsSCSoundFileView.selectionStart(0);
		("INFO: frame selected: " ++ tmx).postln;
		this.itsTmax2Num.value 	= tmx / this.itsSR;
		this.itsVmax2Num.value 	= vmx = this.itsSignal[tmx].abs;
		this.itsFmax2Num.value 	= fmx = this.itsPitch[tmx];
		this.itsSCSoundFileView.timeCursorPosition = tmx;
		
		this.itsSaveButton.value = 1;
	}	// setTmax2Proc

	//---  setTminProc ---------------------------------
	setTminProc{
		tmin = this.itsSCSoundFileView.selectionStart(0);
		("INFO: frame selected: " ++ tmin).postln;
		this.itsTminNum.value 	= tmin / this.itsSR;
		this.itsVminNum.value 	= vmin = this.itsSignal[tmin].abs;
		this.itsFminNum.value 	= fmin = this.itsPitch[tmin];
		this.itsSCSoundFileView.timeCursorPosition = tmin;
		
		this.itsSaveButton.value = 1;
	}	// setTminProc

	//--- normalizeProc -------------------------------

	normalizeProc {
		var return, normalizedFileName, targetdB, target;
		var s;

		this.itsRefreshButton.value = 1;

		s = Server.default;

		targetdB = -3; // -3dB
		target = exp(targetdB/20);

		normalizedFileName = this.itsSoundFileName ++ ".normalized.wav".asString;

		SoundFile.normalize(path:this.itsSoundFileName,outPath:normalizedFileName,maxAmp:target);

		this.itsSoundFileName = normalizedFileName;

		this.itsBuffer = Buffer.read(s,path:this.itsSoundFileName);

		this.updateView(nil);

		this.itsRefreshButton.value = 0;

	}	// normalizeProc

	//-------------------------------------------------
	//-------------- local functions ------------------
	//-------------------------------------------------

	//--- updateView ----------------------------------

	updateView  {
		// Update the signal view window
		this.itsSoundFile = SoundFile.new;
		this.itsSoundFile.openRead(this.itsSoundFileName);

		// Update the length value on the window
		this.itsSCSoundFileView.soundfile = this.itsSoundFile;
		this.itsSCSoundFileView.read(0, this.itsSoundFile.numFrames, 64, true); //startframe, frames, block, closeFile
		this.itsSCSoundFileView.refresh;

		this.itsTmax1Num.value = this.tmax / this.itsSR;
		this.itsFmax1Num.value = this.fmax;
		this.itsVmax1Num.value = this.vmax;
		this.itsTmax2Num.value = this.tmx / this.itsSR;
		this.itsFmax2Num.value = this.fmx;
		this.itsVmax2Num.value = this.vmx;
		this.itsTminNum.value = this.tmin / this.itsSR;
		this.itsFminNum.value = this.fmin;
		this.itsVminNum.value = this.vmin;
		this.itsLenNum.value = this.len;
	}	// updateView


	//--- find maximum in the 1st half of the samples ----
	findtmax1Func {
		var tmax = 0, max = 0, length = 0, sample;

		length = this.itsSignal.size-2;
		for(0, (length / 2).asInt,
			{arg i;

			sample = this.itsSignal.at(i);
			if (sample > max,
				{
				max = sample;
				tmax = i;
				});	// if
			});	// for
		postf("INFO: max1 sample[%] = %\n",tmax,max);

		^tmax;
	}	// END findtmax1Func

	//--- find maximum in the 2nd half of the samples ----
	findtmax2Func {
		var tmx = 0, mx = 0,halfLength = 0, sample;

		halfLength = ((this.itsSignal.size-2)/2).asInt;
		halfLength.reverseDo(		// rueckwaerts
			{arg i;

			sample = this.itsSignal.at(i + halfLength);
			if (sample > mx,
				{
				mx = sample;
				tmx = i + halfLength;
				});	// if
			});	// for
		postf("INFO: max2 sample[%] = %\n",tmx,mx);

		^tmx;
	}	// END findtmax2Func

	//--- find stable minimum ---

	findtminFunc {

		var tmin = 0, min = 1, length, sample, prevSample, diff, tmp;

		length = this.itsSignal.size-2;
		for(1, length.asInt,
			{arg i;

			prevSample = this.itsSignal.at(i-1);
			sample = this.itsSignal.at(i);
			diff = sample - prevSample;
			// if < current min and not totally null
			if ((sample < min) && (sample >= 0.2),	
				{
				// If the signal is also very stable, set the new position
				if(diff < 0.00001,			// Wendepunkt
					{
					
					min = sample;
					tmin = i;
				});	// if
			});	// if
		});	// for
		
		postf("INFO: min sample[%] = %\n",tmin,min);

		^tmin;
	}	// END findtminFunc



	//--- readConfigFile ---

	readConfigFile {
		var aFile;

		aFile = File(this.itsConfigFileName,"rb");

		if (aFile.isOpen == true, {
			this.tmax = aFile.getFloat;
			this.tmx = aFile.getFloat;
			this.tmin = aFile.getFloat;
			this.fmax = aFile.getFloat;
			this.fmx = aFile.getFloat;
			this.fmin = aFile.getFloat;
			this.vmax = aFile.getFloat;
			this.vmx = aFile.getFloat;
			this.vmin = aFile.getFloat;
			this.len = aFile.getFloat;
			("INFO: " ++ this.itsConfigFileName ++ " loaded").postln;
		},
		{
			("ERROR: File '" ++ this.itsConfigFileName ++ "' could not be opened").postln;
		});
		aFile.close;
	}

	//--- writeConfigFile ---

	writeConfigFile {
		var aFile;

		aFile = File(this.itsConfigFileName,"wb");

		if (aFile.isOpen == true, {
			aFile.putFloat(this.tmax);
			aFile.putFloat(this.tmx);
			aFile.putFloat(this.tmin);
			aFile.putFloat(this.fmax);
			aFile.putFloat(this.fmx);
			aFile.putFloat(this.fmin);
			aFile.putFloat(this.vmax);
			aFile.putFloat(this.vmx);
			aFile.putFloat(this.vmin);
			aFile.putFloat(this.len);
			("INFO: " ++ this.itsConfigFileName ++ " saved").postln;
		});
		aFile.close;
	}

	//--- writeConfigFile ---

	resetConfig {
		this.tmax = 0;
		this.tmx = 0;
		this.tmin = 0;
		this.fmax = 0;
		this.fmx = 0;
		this.fmin = 0;
		this.vmax = 0;
		this.vmx = 0;
		this.vmin = 0;
		this.len = 0;
		
		
		this.itsTmax1Num.value = 0;
		this.itsFmax1Num.value = 0;
		this.itsVmax1Num.value = 0;
		this.itsTmax2Num.value = 0;
		this.itsFmax2Num.value = 0;
		this.itsVmax2Num.value = 0;
		this.itsTminNum.value = 0;
		this.itsFminNum.value = 0;
		this.itsVminNum.value = 0;
		this.itsLenNum.value = 0;
	}
	
	//--- getSignal ---

	getSignal {
		var return;
		// Open file (read header only)
		this.itsSoundFile = SoundFile.new;
		return = this.itsSoundFile.openRead(this.itsSoundFileName);

		if (return == true,
			{
				// Allocate memory for the files (32 bit floating point Array)
				this.itsSignal = FloatArray.newClear(this.itsSoundFile.numFrames);

				// Copy the sound file samples into the "itsSignal" array
				this.itsSoundFile.readData(this.itsSignal);
			},
			{
				("ERROR: problem reading " ++ this.itsSoundFileName).postln;
				return.postln;
			}
		); // end if

		// Copy the signal and take the absolute value
		this.itsAbsSignal = this.itsSignal.deepCopy;
		this.itsAbsSignal = this.itsAbsSignal.abs;

		// Filter signal (average on 6 samples)
		for (5, this.itsAbsSignal.size - 7,
			{arg i;
			// mean on 6 samples
			this.itsAbsSignal.put(i-5,
				(this.itsAbsSignal.at(i-5) +
				this.itsAbsSignal.at(i-4) +
				this.itsAbsSignal.at(i-3) +
				this.itsAbsSignal.at(i-2) +
				this.itsAbsSignal.at(i-1) +
				this.itsAbsSignal.at(i)) / 6.0);
			}
		);	// END for


		this.itsSoundFile.close;


	}

	//--- getPitch ---

	getPitch {
		this.itsPitch = FloatArray.newClear(this.itsSignal.size);
		// Open pitch file and copy the content to an Array
		this.itsPitchFile = SoundFile.new;
		this.itsPitchFile.openRead(this.itsPitchFileName);
		this.itsPitchFile.readData(this.itsPitch);
		this.itsPitchFile.close;
	}

}	// END SampleView
