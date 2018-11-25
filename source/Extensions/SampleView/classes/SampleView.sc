//--- Soliloque SampleView ----------------------------
//--- (c) 2002-2018 by Thomas Seelig and Frederic Roskam
//--- seelig@snafu.de ---------------------------------
//--- froskam@gmail.com--------------------------------

SampleView {
    // Instance variables: var a, <b, >c, <>d; a has no getter or setter.
    // => b has a getter but not a setter. c has only a setter. d has both a getter and setter.
    classvar <version = "8.01";

    classvar kButtonWidth  = 80;
    classvar kButtonHeight = 18;

    var <>maxpos1 = 0,     // position of the max in the 1st part (in Samples !)  TODO: rename as nmax
    <>maxamp1  = 0.0,
    <>maxfreq1 = 0.0,
    <>maxpos2  = 0,        // position of the max in the 2nd part (in Samples !)
    <>maxfreq2 = 0.0,
    <>maxamp2  = 0.0,
    <>minpos   = 0,        // position of the min (in Samples !)
    <>minamp   = 0.0,
    <>minfreq  = 0.0,
    <>len      = 0.0;      // File length in seconds

    var <>soundFileView,   // View of the sound file
    <>loadButton,        // Load button
    <>prepareButton,     // Make button
    <>playbackButton,    // Play button
    <>analyzeButton,     // Analyse button
    <>saveButton,
    <>setTmax1Button,    // Set Tmax button
    <>setTmax2Button,    // Set Tmx button
    <>setTminButton,     // Set Tmin button
    <>refreshButton,     // Normalize button
    <>cropButton,        // Crop button
    <>tmax1NumberBox = 0, // Tmax
    <>fmax1NumberBox = 0, // Fmax
    <>vmax1NumberBox = 0, // Vmax
    <>tmax2NumberBox = 0, // Tmx
    <>fmax2NumberBox = 0, // Fmx
    <>vmax2NumberBox = 0, // Vmx
    <>tminNumberBox = 0,  // Tmin
    <>fminNumberBox = 0,  // Fmin
    <>vminNumberBox = 0,  // Vmin
    <>lenNumberBox  = 0,  // LenNu
    <>soundFileName,     // Sound file name
    <>soundFile,         // Sound file
    <>soundFileArray,    // Sound (signal itself)
    <>soundFileArrayAbs, // Absolute version of the signal
    <>pitchFile,         // Pitch file
    <>pitchFileName,     // Pitch file name
    <>bufferPitch,       // Pitch buffer
    <>pitchArray,        // Pitch array (data)
    <>configFileName,    // Config file name
    <>sampleRate,        // Server sample rate (Hz)
    <>buffer = nil;

    //---------- new --------------------------------------------------------------

    *new {
        arg aWindow,
        aXOffset = 0,
        aYOffset = 0,
        aLabel = "Sample x",
        aSoundFileName;

        ^super.new.init(aWindow, aXOffset, aYOffset, aLabel, aSoundFileName);
    }   // new


    init {
        arg aWindow,
        aXOffset,
        aYOffset,
        aLabel,
        aSoundFileName;

        var s,a;

        s = Server.default;
        this.sampleRate = s.sampleRate;
        ("INFO: Initialize " ++ aSoundFileName).postln;

        // Create buttons and text for the current sample
        this.createViews(aWindow, aLabel, aXOffset, aYOffset);

        // Define actions (functions) for each button
        this.loadButton.action =     { this.loadProc };
        this.prepareButton.action =  { this.prepareProc };
        this.playbackButton.action = { this.playProc };
        this.analyzeButton.action =  { this.analyzeProc };
        this.saveButton.action =     { this.saveProc };
        this.setTmax1Button.action = { this.setTmax1Proc };
        this.setTmax2Button.action = { this.setTmax2Proc };
        this.setTminButton.action =  { this.setTminProc };
        this.refreshButton.action =  { this.updateView };

        // Check for the presence of default files
        this.soundFileName = aSoundFileName;
        this.pitchFileName = this.soundFileName ++ ".pitch".asString;
        this.configFileName = this.soundFileName ++ ".cfg".asString;

		//  open existing config file
		this.readConfigFile(nil);
		{ this.updateView(nil) }.defer(0.1);

        //  open existing sound files
        this.buffer = Buffer.read(s, this.soundFileName,
            action: {
                postf("INFO: % loaded\n",this.soundFileName);

                //  open existing pitch files
                this.bufferPitch = Buffer.read(s, this.pitchFileName,
                    action: {
                        postf("INFO: % loaded\n",this.pitchFileName);
                        this.getSignal(nil);
                        postf("INFO: % -> Array\n",this.soundFileName);
                        this.getPitch(nil);
                        postf("INFO: % -> Array\n",this.pitchFileName);

                    }
                );


            }
        );

    }   // init


    createViews {
        arg aWindow, title, aXOffset, aYOffset, st;

        // Wave form window (and options)
        this.soundFileView                 = SoundFileView.new( aWindow, Rect(aXOffset, aYOffset, 200, 95));
        this.soundFileView.timeCursorOn    = true;
        this.soundFileView.timeCursorColor = Color.red;
        this.soundFileView.gridColor       = Color.gray;
        this.soundFileView.timeCursorPosition= 0;
        this.soundFileView.drawsWaveForm   = true;
        this.soundFileView.gridOn          = true;
        this.soundFileView.gridResolution  = 1.0; // 1 second
        // text : sample #
        st = StaticText( aWindow, Rect.new(5 + aXOffset, aYOffset, 128, 20));
        st.string = title;
        st.stringColor = Color.white;
        //--- Values for Tmax, Tmx ... --------------
        this.tmax1NumberBox        = NumberBox( aWindow, Rect.new(210 + aXOffset, 21 + aYOffset, 45, 14));
        this.fmax1NumberBox        = NumberBox( aWindow, Rect.new(210 + aXOffset, 39 + aYOffset, 45, 14));
        this.vmax1NumberBox        = NumberBox( aWindow, Rect.new(210 + aXOffset, 57 + aYOffset, 45, 14));
        this.tmax2NumberBox        = NumberBox( aWindow, Rect.new(310 + aXOffset, 21 + aYOffset, 45, 14));
        this.fmax2NumberBox        = NumberBox( aWindow, Rect.new(310 + aXOffset, 39 + aYOffset, 45, 14));
        this.vmax2NumberBox        = NumberBox( aWindow, Rect.new(310 + aXOffset, 57 + aYOffset, 45, 14));
        this.tminNumberBox         = NumberBox( aWindow, Rect.new(410 + aXOffset, 21 + aYOffset, 45, 14));
        this.fminNumberBox         = NumberBox( aWindow, Rect.new(410 + aXOffset, 39 + aYOffset, 45, 14));
        this.vminNumberBox         = NumberBox( aWindow, Rect.new(410 + aXOffset, 57 + aYOffset, 45, 14));
        //this.lenNumberBox        = NumberBox( aWindow, Rect.new(210 + aXOffset, aYOffset, 45, 14));
        //--- Buttons with authorized states -----------------------
        this.loadButton          = Button( aWindow, Rect.new(aXOffset, 100 + aYOffset, kButtonWidth, kButtonHeight));
        this.loadButton.states   = [["load...",Color.black,Color.new255(249,182,172)]];

        this.playbackButton      = Button( aWindow, Rect.new(120 + aXOffset, 100 + aYOffset, kButtonWidth, kButtonHeight));
        this.playbackButton.states = [["play",Color.black,Color.new255(177,237,105)]];

        this.prepareButton       = Button( aWindow, Rect.new(210 + aXOffset, aYOffset, kButtonWidth, kButtonHeight));
        this.prepareButton.states= [["prepare",Color.white,Color.black]];

        this.analyzeButton       = Button( aWindow, Rect.new(310 + aXOffset, aYOffset, kButtonWidth, kButtonHeight));
        this.analyzeButton.states = [["analyse",Color.white,Color.black],["busy...",Color.red,Color.black],["",Color.gray,Color.gray]];

        this.saveButton          = Button( aWindow, Rect.new(410 + aXOffset, 100 + aYOffset, kButtonWidth, kButtonHeight));
        this.saveButton.states = [["save",Color.white,Color.gray],["save",Color.white,Color.new255(231,160,40)]];

        this.setTmax1Button      = Button( aWindow, Rect.new(210 + aXOffset, 78 + aYOffset, kButtonWidth, kButtonHeight));
        this.setTmax1Button.states = [["set tmax1",Color.white,Color.black]];
        this.setTmax2Button      = Button( aWindow, Rect.new(310 + aXOffset, 78 + aYOffset, kButtonWidth, kButtonHeight));
        this.setTmax2Button.states = [["set tmax2",Color.white,Color.black]];
        this.setTminButton       = Button( aWindow, Rect.new(410 + aXOffset, 78 + aYOffset, kButtonWidth, kButtonHeight));
        this.setTminButton.states = [["set tmin",Color.white,Color.black]];

        this.refreshButton   = Button( aWindow, Rect.new(410 + aXOffset, aYOffset, kButtonWidth, kButtonHeight));
        this.refreshButton.states = [["refresh",Color.white,Color.black]];
        //--- Extra text ------------------
        StaticText( aWindow, Rect.new(260 + aXOffset, 21 + aYOffset, 47, 14)).string = "tmax1";
        StaticText( aWindow, Rect.new(260 + aXOffset, 39 + aYOffset, 47, 14)).string = "freq";
        StaticText( aWindow, Rect.new(260 + aXOffset, 57 + aYOffset, 47, 14)).string = "amp";
        StaticText( aWindow, Rect.new(360 + aXOffset, 21 + aYOffset, 47, 14)).string = "tmax2";
        StaticText( aWindow, Rect.new(360 + aXOffset, 39 + aYOffset, 47, 14)).string = "freq";
        StaticText( aWindow, Rect.new(360 + aXOffset, 57 + aYOffset, 47, 14)).string = "amp";
        StaticText( aWindow, Rect.new(460 + aXOffset, 21 + aYOffset, 47, 14)).string = "tmin";
        StaticText( aWindow, Rect.new(460 + aXOffset, 39 + aYOffset, 47, 14)).string = "freq";
        StaticText( aWindow, Rect.new(460 + aXOffset, 57 + aYOffset, 47, 14)).string = "amp";
        this.lenNumberBox = StaticText( aWindow, Rect.new(5 + aXOffset, 78 + aYOffset, kButtonWidth, 14));
        lenNumberBox.stringColor = Color.white;
        this.lenNumberBox.string = format("% seconds",this.len.round(0.1));

    }   // end createViews


    loadProc {
        var newSoundFile;   // Sound file to create
        var s;

        s = Server.default;

        FileDialog.new(
            { // If correct file name returned
                arg inputSoundFileName;

                var return;
                var sampleRate;     // Sample rate
                var signalLength;   // number of frames

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
                    File.delete(this.soundFileName);
                    File.delete(this.pitchFileName);
                    //File.delete(this.configFileName);
                    // Reset config for this sample
                    this.resetConfig(nil);

                    // Get sound file sample rate
                    sampleRate = newSoundFile.sampleRate;
                    ("INFO: Sound file sample rate:" ++ sampleRate ++ " Hz" ).postln;

                    // Limit sound length to 60 seconds
                    if ( newSoundFile.duration > 60,
                        // then
                        { signalLength = sampleRate * 60; "WARNING: max file length is 60s".postln; },
                        // else
                        { signalLength = newSoundFile.numFrames; }
                    );

                    // Update the length value on the window
                    //this.lenNumberBox.value = signalLength / sampleRate;
                    this.len = signalLength / sampleRate;
                    this.lenNumberBox.string = format("% seconds",this.len.round(0.1));

                    // Copy sound file in the buffer
                    "INFO: if sound file is stereo, left channel is only kept".postln;
                    this.buffer = Buffer.readChannel(s, path:newSoundFile.path, numFrames:signalLength, channels: [0]);

                    // Update the signal view window
                    this.soundFileView.soundfile = newSoundFile;
                    this.soundFileView.read(0, signalLength, 64, true); //startframe, frames, block, closeFile
                    this.soundFileView.refresh;

                    // Copy sound file to disk
                    this.buffer.write(this.soundFileName,"wav","int16",
                        completionMessage: {
                            postf("INFO: % written to the disk\n",this.soundFileName);
                            postf("INFO: Please click on the [ANALYSE] button for this sample\n");
                        },
                        leaveOpen:false
                    );

                    this.analyzeButton.value = 2;
                },
                {
                    "ERROR: problem reading the sound file".postln;
                    return.postln;
                }
                ); // end if


            },
            { // if invalid file selection
                    "WARNING: no sound file selected".postln;
            },
            // max number of files allowed to be selected
            1
        ); // end FileDialog.new
    }   // END loadProc


    playProc {
        var transp = 1, size, start, end;
        var s, b, return, nextSynth,x;

        s = Server.default;

        // If no Synth is playing
        if (s.numSynths == 0,
        {
          // Play
          postf("INFO: Play %\n",this.soundFileName);
          // Get start frame and end frame
          start = this.soundFileView.selectionStart(0);
          size  = this.soundFileView.selectionSize(0);

          if (size>0,
          {
            // Play selection
            SynthDef("playfile",{
              arg out = 0,
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
                  this.buffer.bufnum,
                  //trigger:0,
                  rate:BufRateScale.kr(this.buffer.bufnum),
                  startPos:start)
                );
              }).play(s,[\start,start,\tscale,size/this.buffer.sampleRate]);

            postf("INFO: Play selection (% sec)\n",size/this.buffer.sampleRate);

            },
            {
              "INFO: Play whole file".postln;
              this.buffer.play;
          }
          );
          },
          {
            // Stop
            "INFO: Stop".postln;
            Server.freeAll;
        }
        ); // end if
    }   // playProc


    prepareProc{
        var soundLength; // sound file length in seconds
        var s, node;

        this.analyzeButton.value = 2;

        s = Server.default;

        // Beginning of a Routine
        {
            soundLength = this.buffer.numFrames/this.buffer.sampleRate;
            this.bufferPitch = Buffer.alloc(s,this.buffer.numFrames);
            SynthDef("PitchFollow",
                {
                    arg buffin = 0, buffout = 1;

                    var out = 0;
                    var x, freq;

                    // Save frequencies into the output signal
                    freq = K2A.ar(Pitch.kr(PlayBuf.ar(1, buffin, BufRateScale.kr(buffin)),median:1,minFreq:20,maxFreq:800)).at(0);
                    x = RecordBuf.ar(freq, buffout);
                    // Optionnal
                    Out.ar( out, PlayBuf.ar(1, buffin, BufRateScale.kr(buffin)));
                }
            ).send(s);

            // Wait for the Synth Def to be transmitted
            2.wait;

            // Run Synth
            postf("INFO: Prepare %\n", this.soundFileName);
            node = Synth(\PitchFollow, [\buffin, this.buffer.bufnum, \buffout, this.bufferPitch.bufnum]);
            soundLength.wait;

            // Save pitch signal into a file
            this.bufferPitch.write(this.pitchFileName, headerFormat: 'Sun', sampleFormat:'float32');

            postf("INFO: % prepared. Please click on [Analyse] \n",this.soundFileName);
            node.free;

        }.fork;

        soundLength = this.buffer.numFrames/this.buffer.sampleRate;

        {this.analyzeButton.value = 0;}.defer(soundLength+2);

    }   // prepareProc


    analyzeProc {
        var tsec, i, t, v, f;
        var d, s, return;

        this.analyzeButton.value = 1;

        s = Server.default;

        // Copy the sound file into a local array
        this.getSignal(nil);
        // Copy the pitch file into a local array
        this.getPitch(nil);

        // maxpos1 (samples) = maximum amplitude position of the signal in the 1st half
        maxpos1                    = this.findtmax1Func(this.soundFileArrayAbs);
        maxpos1.postln;
        this.tmax1NumberBox.value  = maxpos1 / this.sampleRate;
        // maxamp1 = amplitude at maxpos1
        this.vmax1NumberBox.value  = maxamp1 = (this.soundFileArrayAbs.at(maxpos1));
        // maxfreq1 = frequency at maxpos1
        this.fmax1NumberBox.value  = maxfreq1 = (this.pitchArray.at(maxpos1));


        // maxpos2 (samples) = maximum amplitude position of the signal in the 2nd half
        maxpos2                     = this.findtmax2Func.value(this.soundFileArrayAbs);
        tsec                    = maxpos2 / this.sampleRate;
        this.tmax2NumberBox.value  = tsec;
        this.vmax2NumberBox.value  = maxamp2 = this.soundFileArrayAbs.at(maxpos2);
        this.fmax2NumberBox.value  = maxfreq2 = this.pitchArray.at(maxpos2);

        // minpos (samples) = minimum amplitude position of the signal between the maxpos1 and maxpos2
        minpos                = this.findtminFunc.value(this.soundFileArrayAbs, maxpos2, maxpos1);
        tsec                = minpos / this.sampleRate;
        this.tminNumberBox.value   = tsec;
        this.vminNumberBox.value   = minamp = this.soundFileArrayAbs.at(minpos);
        this.fminNumberBox.value   = minfreq = this.pitchArray.at(minpos);
        len                     = (this.soundFileArray.size - 2) / this.sampleRate;

        //this.lenNumberBox.value = len;
        this.lenNumberBox.string = format("% seconds",this.len.round(0.1));

        this.analyzeButton.value = 0;

        // Save values
        this.writeConfigFile(nil);
        postf("INFO: Analysis done. You may want to tweak the automatic results by using the [set tmax1], [set tmax2] and [set minpos] buttons\n",this.soundFileName);

    }   // analyzeProc


    saveProc {
        // Save values
        this.writeConfigFile(nil);
        this.updateView(nil);
        "INFO: Configuration saved".postln;
    }   // saveProc


    setTmax1Proc {
        maxpos1 = this.soundFileView.selectionStart(0);

        ("INFO: frame selected: " ++ maxpos1).postln;

        if (this.soundFileArray.isNil,
        {
            "ERROR: Please re-prepare and analyse first".postln;
        },
        {
            this.tmax1NumberBox.value  = maxpos1 / this.sampleRate;
            this.vmax1NumberBox.value  = maxamp1 = this.soundFileArray[maxpos1].abs;
            this.fmax1NumberBox.value  = maxfreq1 = this.pitchArray[maxpos1];
            this.soundFileView.timeCursorPosition = maxpos1;
        }
        );

        this.saveButton.value = 1;
    }   // setTmax1Proc


    setTmax2Proc {
        maxpos2 = this.soundFileView.selectionStart(0);
        ("INFO: frame selected: " ++ maxpos2).postln;
        this.tmax2NumberBox.value  = maxpos2 / this.sampleRate;
        this.vmax2NumberBox.value  = maxamp2 = this.soundFileArray[maxpos2].abs;
        this.fmax2NumberBox.value  = maxfreq2 = this.pitchArray[maxpos2];
        this.soundFileView.timeCursorPosition = maxpos2;

        this.saveButton.value = 1;
    }   // setTmax2Proc


    setTminProc{
        minpos = this.soundFileView.selectionStart(0);
        ("INFO: frame selected: " ++ minpos).postln;
        this.tminNumberBox.value   = minpos / this.sampleRate;
        this.vminNumberBox.value   = minamp = this.soundFileArray[minpos].abs;
        this.fminNumberBox.value   = minfreq = this.pitchArray[minpos];
        this.soundFileView.timeCursorPosition = minpos;

        this.saveButton.value = 1;
    }   // setTminProc


    normalizeProc {
        var return, normalizedFileName, targetdB, target;
        var s;

        this.refreshButton.value = 1;

        s = Server.default;

        targetdB = -3; // -3dB
        target = exp(targetdB/20);

        normalizedFileName = this.soundFileName ++ ".normalized.wav".asString;

        SoundFile.normalize(path:this.soundFileName, outPath:normalizedFileName, maxAmp:target);

        this.soundFileName = normalizedFileName;

        this.buffer = Buffer.read(s,path:this.soundFileName);

        this.updateView(nil);

        this.refreshButton.value = 0;

    }   // normalizeProc


    //-------------------------------------------------------------------------
    //                            local functions
    //-------------------------------------------------------------------------

    updateView  {
        // Update the signal view window
        this.soundFile = SoundFile.new;
        this.soundFile.openRead(this.soundFileName);

        // Update the length value on the window
        this.soundFileView.soundfile = this.soundFile;
        this.soundFileView.read(0, this.soundFile.numFrames, 64, true); //startframe, frames, block, closeFile
        this.soundFileView.refresh;

        this.tmax1NumberBox.value = this.maxpos1 / this.sampleRate;
        this.fmax1NumberBox.value = this.maxfreq1;
        this.vmax1NumberBox.value = this.maxamp1;
        this.tmax2NumberBox.value = this.maxpos2 / this.sampleRate;
        this.fmax2NumberBox.value = this.maxfreq2;
        this.vmax2NumberBox.value = this.maxamp2;
        this.tminNumberBox.value = this.minpos / this.sampleRate;
        this.fminNumberBox.value = this.minfreq;
        this.vminNumberBox.value = this.minamp;
        //this.lenNumberBox.value = this.len;
        this.lenNumberBox.string = format("% seconds",this.len.round(0.1));
    }   // updateView


    //--- find maximum in the 1st half of the samples ----
    findtmax1Func {
        var maxpos1 = 0, max = 0, length = 0, sample;

        length = this.soundFileArray.size-2;
        for(0, (length / 2).asInt,
            {
                arg i;

            sample = this.soundFileArray.at(i);
            if (sample > max,
            {
              max = sample;
              maxpos1 = i;
            }); // if
        }); // for
        postf("INFO: maxpos1 sample[%] = %\n",maxpos1,max);

        ^maxpos1;
    }   // END findtmax1Func

    // Find maximum in the 2nd half of the samples
    findtmax2Func {
        var maxpos2 = 0, mx = 0,halfLength = 0, sample;

        halfLength = ((this.soundFileArray.size-2)/2).asInt;
        halfLength.reverseDo(       // rueckwaerts
        {
            arg i;

            sample = this.soundFileArray.at(i + halfLength);
            if (sample > mx,
            {
                mx = sample;
                maxpos2 = i + halfLength;
            }); // if
        }); // for

        postf("INFO: maxpos2 sample[%] = %\n",maxpos2,mx);

        ^maxpos2;
    }   // END findtmax2Func


    // Find stable minimum
    findtminFunc {
        var minpos = 0, min = 1, length, sample, prevSample, diff, tmp;

        length = this.soundFileArray.size-2;
        for(1, length.asInt,
          {arg i;

            prevSample = this.soundFileArray.at(i-1);
            sample = this.soundFileArray.at(i);
            diff = sample - prevSample;
            // if < current min and not totally null
            if ((sample < min) && (sample >= 0.2),
            {
              // If the signal is also very stable, set the new position
              if(diff < 0.00001,          // Wendepunkt
              {

                min = sample;
                minpos = i;
                }); // if
              }); // if
        }); // for

        postf("INFO: min sample[%] = %\n",minpos,min);

        ^minpos;
    }   // END findtminFunc


    readConfigFile {
        var aFile;

        aFile = File(this.configFileName,"rb");

        if (aFile.isOpen == true, {
            this.maxpos1 = aFile.getFloat;
            this.maxpos2 = aFile.getFloat;
            this.minpos = aFile.getFloat;
            this.maxfreq1 = aFile.getFloat;
            this.maxfreq2 = aFile.getFloat;
            this.minfreq = aFile.getFloat;
            this.maxamp1 = aFile.getFloat;
            this.maxamp2 = aFile.getFloat;
            this.minamp = aFile.getFloat;
            this.len = aFile.getFloat;
            ("INFO: " ++ this.configFileName ++ " loaded").postln;
        },
        {
            ("ERROR: File '" ++ this.configFileName ++ "' could not be opened").postln;
        });
        aFile.close;
    }


    writeConfigFile {
        var aFile;

        aFile = File(this.configFileName,"wb");

        if (aFile.isOpen == true,
            {
                aFile.putFloat(this.maxpos1);
                aFile.putFloat(this.maxpos2);
                aFile.putFloat(this.minpos);
                aFile.putFloat(this.maxfreq1);
                aFile.putFloat(this.maxfreq2);
                aFile.putFloat(this.minfreq);
                aFile.putFloat(this.maxamp1);
                aFile.putFloat(this.maxamp2);
                aFile.putFloat(this.minamp);
                aFile.putFloat(this.len);
                ("INFO: " ++ this.configFileName ++ " saved").postln;
            }
        );

        aFile.close;
    }


    resetConfig {
        this.maxpos1 = 0;
        this.maxpos2 = 0;
        this.minpos = 0;
        this.maxfreq1 = 0;
        this.maxfreq2 = 0;
        this.minfreq = 0;
        this.maxamp1 = 0;
        this.maxamp2 = 0;
        this.minamp = 0;
        this.len = 0.0;


        this.tmax1NumberBox.value = 0;
        this.fmax1NumberBox.value = 0;
        this.vmax1NumberBox.value = 0;
        this.tmax2NumberBox.value = 0;
        this.fmax2NumberBox.value = 0;
        this.vmax2NumberBox.value = 0;
        this.tminNumberBox.value = 0;
        this.fminNumberBox.value = 0;
        this.vminNumberBox.value = 0;
        //this.lenNumberBox.value = 0;
        this.lenNumberBox.string = format("% seconds",this.len.round(0.1));
    }


    getSignal {
        var return;
        // Open file (read header only)
        this.soundFile = SoundFile.new;
        return = this.soundFile.openRead(this.soundFileName);

        if (return == true,
            {
                // Allocate memory for the files (32 bit floating point Array)
                this.soundFileArray = FloatArray.newClear(this.soundFile.numFrames);

                // Copy the sound file samples into the "soundFileArray" array
                this.soundFile.readData(this.soundFileArray);
            },
            {
                ("ERROR: problem reading " ++ this.soundFileName).postln;
                return.postln;
            }
        ); // end if

        // Copy the soundFileArray and take the absolute value
        this.soundFileArrayAbs = this.soundFileArray.deepCopy;
        this.soundFileArrayAbs = this.soundFileArrayAbs.abs;

        // Filter signal (average on 6 samples)
        for (5, this.soundFileArrayAbs.size - 7,
            {arg i;
                // mean on 6 samples
                this.soundFileArrayAbs.put(i-5,
                (this.soundFileArrayAbs.at(i-5) +
                  this.soundFileArrayAbs.at(i-4) +
                  this.soundFileArrayAbs.at(i-3) +
                  this.soundFileArrayAbs.at(i-2) +
                  this.soundFileArrayAbs.at(i-1) +
                  this.soundFileArrayAbs.at(i)) / 6.0);
            }
        );  // END for


        this.soundFile.close;
    }


    getPitch {
        this.pitchArray = FloatArray.newClear(this.soundFileArray.size);
        // Open pitch file and copy the content to an Array
        this.pitchFile = SoundFile.new;
        this.pitchFile.openRead(this.pitchFileName);
        this.pitchFile.readData(this.pitchArray);
        this.pitchFile.close;
    }

}   // END SampleView
