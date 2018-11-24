//******************************************************************************************************
//                                                                                                     *
// Soliloque sur [X, X, X et X] (2002), commentaire par un ordinateur d'un concert mal compris de lui  *
//                                                                                                     *
//                  Last update: Nov. 2018 for SuperCollider 3.9.3 32-bit                              *
// (c) 2002, 2005, 2006, 2018 by Fabien Levy, Thomas Seelig and Frederic Roskam. All rights reserved.  *
//                                                                                                     *
//******************************************************************************************************
//  Requirement:                                                                                       *
//  - SuperCollider 9.3 for Windows 32-bit or macOS                                                    *
//  - Extension: SampleView                                                                           *
//******************************************************************************************************

// Global variables and fonctions
var sampleRate,
numChnls,
soundsPath,
// GUI
w,
sampleView1,
sampleView2,
sampleView3,
sampleView4,
sampleView5,
sampleView6,
theChannelsDlg,
theGoButton,
theStopButton,
texttime,
fulltexttime,
durationText,
channelNumberBox,
// Various functions
rect, sawDown, tri, sawUp, recti, sawDowni, sawUpi, fadeIn, envelopes,
cosFunc, sinFunc, rampFunc, compFunc1,
// Sound files
soundFile1, soundFile2, soundFile3, soundFile4, soundFile5, soundFile6,
// From sound file analysis
tmax1,  tmax2, tmax3, tmax4, tmax5, tmax6,  // Time where the Max of Sample is between [O 50%]
tmx1,  tmx2, tmx3, tmx4, tmx5, tmx6,        // Time where the Max of Sample is between [50% 100 %]
tmin1,  tmin2, tmin3, tmin4, tmin5, tmin6,  // Time where the Min of Sample is between [tmax und tmx]
fmax1,  fmax2, fmax3, fmax4, fmax5, fmax6,  // Average Frequence of the Max of the Samples for tmax
fmx1,  fmx2, fmx3, fmx4, fmx5, fmx6,        // Average Frequenz of the Max of the Samples for tmx
fmin1,  fmin2, fmin3, fmin4, fmin5, fmin6,  // Average Frequenz of the Min of the Samples for tmin
vmax1, vmax2, vmax3, vmax4, vmax5, vmax6,   // Volume of Max of the Samples 1-6 for tmax
vmx1, vmx2, vmx3, vmx4, vmx5, vmx6,         // Volume of Max of the Samples 1-6 for tmx
vmin1,  vmin2, vmin3, vmin4, vmin5, vmin6,  // Volume of Min of the Samples 1-6
l1, l2, l3, l4, l5, l6,                     // Length of the Pattern
lh1, lh2, lh3, lh4, lh5, lh6,               // Random Parameter calculated from l1 l6
minmax1, minmax2, minmax3, minmax4, minmax5, minmax6, // valeurs de distance minimale
minmx1, minmx2, minmx3, minmx4, minmx5, minmx6,       // valeurs de distance minimale
minmin1, minmin2, minmin3, minmin4, minmin5, minmin6, // valeurs de distance minimale
// Global time
timeOffset = 5.0,
globalT1   = 0,
globalT2   = 0,
globalT31  = 0,
globalT32  = 0,
globalT33  = 0,
globalT4   = 0,
// Main parts
teil1, teil2, teil31, teil32, teil33, teil4, schluss; // Parties 1 a 4 et conclusion


// Initialise values
tmax1 = 0.9; fmax1 = 80;  vmax1 = 1;  tmx1 = 07.84;fmx1 = 80; vmx1 = 1;  tmin1 = 7.45; fmin1 = 160;
vmin1 = 0.1; l1= 13.520;
tmax2 = 4.0; fmax2 = 196; vmax2 = 1;  tmx2 = 05.71;fmx2 = 206;vmx2 = 0.8;tmin2 = 19.0; fmin2 = 800;
vmin2 = 0.3; l2= 19.570;
tmax3 = 0.83;fmax3 = 140; vmax3 = 0.9;tmx3 = 8.4;  fmx3 = 220;vmx3 = 1;  tmin3 = 6.4;  fmin3 = 84;
vmin3 = 0.4; l3= 10.950;
tmax4 = 3.05;fmax4 = 99;  vmax4 = 0.7;tmx4 = 11.69;fmx4 = 332;vmx4 = 1;  tmin4 = 0.7;  fmin4 = 120;
vmin4 = 0.3; l4= 17.550;
tmax5 = 1.28;fmax5 = 898; vmax5 = 1;  tmx5 = 10.6; fmx5 = 760;vmx5 = 0.6;tmin5 = 12.1; fmin5 = 166;
vmin5 = 0.1; l5= 13.130;
tmax6 = 0.5; fmax6 = 1864;vmax6 = 1;  tmx6 = 9.7;  fmx6 = 100;vmx6 = 0.9;tmin6 = 15.00;fmin6 = 139;
vmin6 = 0.4; l6= 16.370;

// End of variable declarations

// clear all variables
Exception.debug = false;
s = Server.local;
s.quit;

// Set default number of channels
numChnls = s.options.numOutputBusChannels;
if (numChnls >= 16, { numChnls = 16; }, nil);

// Wait for server boot
s.waitForBoot({
	// Get current server sample rate
	sampleRate = s.sampleRate;

	//      ******************************************************************************
	//      **********************    III  Instruments       *****************************
	//      ******************************************************************************
	//      --- Temporal envelopes ---------------------------------------------
	envelopes = [
	Env.new(#[1, 1], #[1.0]),                   // rect
	Env.new(#[1, 0], #[1.0]),                   // saw DOWN
	Env.new(#[0, 1, 0], #[1.0, 0.0]),           // saw UP
	Env.new(#[0, 1, 0], #[0.5, 0.5]),           // triangle
	Env.new(#[0, 1, 1, 0], #[0.01, 1.0, 0.01]), // recti
	Env.new(#[0, 1, 0], #[0.01, 1.0]),          // saw DOWN i
	Env.new(#[0, 1, 0], #[1.0, 0.01]),          // saw UP i
	Env.new([0.0001, 1, 1],[0.2, 10],'exponential') // fade IN
	];


	//--- Playback ---------------------------------------------
	// removed instruments from here


	//--- Fonctions du temps ---------------------------------------------
	cosFunc = Pn(           // repeats the enclosed pattern a number of times
		Ppatmod(        // modify Pattern
			Pseq(#[0]),   // because of Syntax necessity
			// function that modifies the pattern
			{arg pat, i; 0.5*(cos(i*2*pi*l2/(10*l3)))}, // Function, i: number of the Iterationen
			inf),     // for ever
		inf
		);       // without End

	sinFunc = Pn(                                              // repeat Pattern
		Ppatmod(                                   // Modify Pattern
			Pseq(#[0]),                         // because of Syntaxnecessity
			{arg pat, i; 0.5*(sin(0.35*i*2*pi*l4/(10*l5)))}, // Function, i: number of the Iterationen
			inf),                              // fuer immer
		inf
		);                                     // ohne Ende

	rampFunc =  Pn(                                              // for position of Samplestart
		Ppatmod(
			Pseq(#[0]),
			{arg pat, i; i * 0.1},               // i like above, 0...0.9 go out, pat won't be used
			inf),                                // infini on peut remplacer par un nombre pour croissance finie
		inf
		);

	compFunc1 = Pn(                                              // Wiederhole Pattern
		Ppatmod(                                   // Modifiziere Pattern
			Pseq(#[0]),                         // Dummy aus Syntaxgruenden
			{arg pat, i; 0.5*(cos((0.00001*(l2/l3)*(i.squared))-(0.0015*(l5/l4)*i))-(squared(sin(sqrt(0.3*(l6/l4)*i))))) },
			inf),                              // fuer immer
		inf
		);                                     // ohne Ende

	//****************************************************************************************
	//********************************      IV-1 SCORE FOR THE FIRST PART       ************
	//****************************************************************************************

	//--------------Score of time (Tzeit = starting points of the cells, Tdur = duration of each)
	teil1 = {
		//------------ variables --------------------------------
		var t1c1p1, t1c1p2, t1c1p3, //t : Teil, c : cell, p : pattern
		t1c2p1, t1c2p2, t1c2p3, t1c3p1, t1c3p2, t1c3p3, t1c4p1, t1c4p2, t1c4p3,
		t1c5p1, t1c5p2, t1c5p3, t1c6p1, t1c6p2, t1c6p3,
		t1c7p1, t1c7p2, t1c7p3, t1c7p4, t1c8p1, t1c8p2, t1c8p3, t1c8p4,
		t1c9p1, t1c9p2, t1c9p3, t1c9p4,t1c10p1, t1c10p2, t1c10p3, t1c10p4,
		t1c11p1, t1c11p2, t1c11p3, t1c11p4,t1c12p1, t1c12p2, t1c12p3, t1c12p4, t1c12p5, t1c12p6,
		t1c13p1, t1c13p2, t1c13p3, t1c13p4,

		t0zeit1, t0zeit2, t0zeit3, t0zeit4, //t0 : Teil 0                   // TEIL 0 et I
		t1zeit1, t1zeit2, t1zeit3, t1zeit4, t1zeit5, t1zeit6, t1zeit7, t1zeit8,
		t1zeit9, t1zeit10, t1zeit11, t1zeit12, t1zeit13,  // Depart time
		t1dur1, t1dur2, t1dur3, t1dur4, t1dur5, t1dur6, t1dur7, t1dur8, t1dur9,
		t1dur10, t1dur11, t1dur12, t1dur13; // Duration time */

		//--------------Partition du temps--------------------------
		t0zeit4 = 0.0; // End of the introduction part (applause)
		t1zeit1 =  t0zeit4; /* beginning of the first part t1cell1 */
		t1zeit2 = t1zeit1 + (2/3)*(l1);           t1dur2 = (l1)/3;
		t1zeit3 = t1zeit1 + l1;             t1dur3 = (l1)/4;
		t1zeit4 = t1zeit3 + (5*(l1)/6);         t1dur4= (l1)/4;
		t1zeit5 = t1zeit4 + t1dur4 +0.001;        t1dur5 = (l4)/5;
		t1zeit6 = t1zeit5 + t1dur5 + lh5;         // t1zeit6 = lh5 +  (l4)/5 +  (l1)/4 + (5*(l1)/6) + l1 = lh5 + (l4)/5 + 25*l1/12
		t1dur6 = ((lh5)+ (lh2))/2;
		t1zeit7 = t1zeit6 + t1dur6 +0.001;        t1dur7 = lh1;
		t1zeit8 = t1zeit7 + t1dur7  +0.001;       t1dur8 = lh3;
		t1zeit9 = t1zeit8 + t1dur8  +0.001;
		t1zeit10 = t1zeit9 + lh5;           t1dur10 = lh6;
		t1zeit11 = t1zeit10 + t1dur10  +0.001;      t1dur11 = lh4;
		t1zeit12 = t1zeit11 + t1dur11  +0.001;      t1dur12 = lh2 + lh3;
		t1zeit13 = t1zeit12 + t1dur12  +0.001;      t1dur13 = 0.35;

		// t1zeit13 =t1zeit6 + lh2 + lh3 + lh4 + lh6+ lh5 +lh3 + lh1 +((lh5)+ (lh2))/2 = (l4)/5 + 25*l1/12 + 25l1/12+lh1 + 3/2lh2 + lh3 + lh4 + 5/3 lh5 + lh6

		t1dur1 = t1zeit13 - t1zeit1 ; // duration of cell 1
		t1dur9 = t1zeit13 - t1zeit9;
		globalT1 = t1zeit13+t1dur13 + 0.1;   // Startzeit fuer Teil II

		//---------------------------------------------------------------------------
		//----------------- Description of each cell of the part T1 ----------------
		//---------------------------------------------------------------------------

		// --- T1Cell 1 (issue de pattern27 tres doux, va de pan 0  pan -1 et 1, et crescendo de 0 a 3)-----
		t1c1p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum,\start, (tmin1 + (0.12*(l1/l3)*(cosFunc + 0.1))),\speed, 5,\len, (0.07*(l1/l3)),\freq, (fmin1*(1.5+cosFunc)),\q, 1.3,\frqEnvIdx, 2,\dur, Pseq([(0.043*(l1/l3))], t1dur1/(0.043*(l1/l3))),\pan, compFunc1,\vol, ((0.4/(t1dur1))*rampFunc) +(0.6*cosFunc),\envIdx, 3);
		t1c1p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum,\start, (tmin3 + (0.1*(l2/l3)*(cosFunc+0.2))),\speed, (5.01*(fmin1/fmin3)),\len, 0.06*(l2/l3),\freq, (fmin3*(1.3+cosFunc)),\q, 1.2,\frqEnvIdx, 2,\dur, Pseq([(0.051*(l2/l3))], (t1dur1/(0.051*(l2/l3)))),\pan, compFunc1,\vol, ((0.4/(t1dur1))*rampFunc) -(0.5*cosFunc),\envIdx, 3);
		t1c1p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum,\start, (tmin5 + (0.16*(l4/l3)*(cosFunc + 0.4))),\speed, (4.96*(fmin1/fmin5)),\len, (0.05*(l4/l3)),\freq, (fmin5*(1.3+cosFunc)),\q, 1.5,\frqEnvIdx, 2,\dur, Pseq([(0.062*(l4/l3))], (t1dur1/(0.062*(l4/l3)))),\pan, compFunc1,\vol, ((0.4/(t1dur1))*rampFunc) +(0.7*cosFunc),\envIdx, 3);
		// ------------T1Cell 2 grave + orchestre aigu -------------------------
		t1c2p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmax1 + ((minmax1)*0.9*(cosFunc+0.1)*(sinFunc+0.4)), \speed, 0.31+(0.15*(cosFunc+0.1)*(0.55 + (0.5*sinFunc))), \len, 0.09,\freq, fmax1, \q, 1.2,\frqEnvIdx, 1, \dur,  Pseq([0.08], t1dur2/0.08), \pan, Pseq(#[0, -1, -0.5, 0.3, 1, 0.7], inf), \vol, (0.5 - (0.2*cosFunc*(sinFunc-0.2))), \envIdx, 1);
		t1c2p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmax2 + ((minmax2)*0.8*(sinFunc-0.2)*(cosFunc+0.9)), \speed, 0.5+(0.2*(sinFunc+0.2)*(cosFunc+0.3)), \len, 0.09,\freq, fmax2, \q, 1.2,\frqEnvIdx, 1, \dur,  Pseq([0.076], t1dur2/0.076), \pan, Pseq(#[-0.5, 1, 0.3, 1, 0.7], inf), \vol,  (0.5 - (0.15*sinFunc*(sinFunc-0.1))), \envIdx, 1);
		t1c2p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, tmin5 + ((minmax5)*0.7*(sinFunc + 0.4)*(sinFunc+0.1)), \speed, 1.8 + (0.25*(cosFunc-0.2)*sinFunc), \len, 0.09,\freq, fmax5, \q, 1.2,\frqEnvIdx, 1, \dur,  Pseq([0.082], t1dur2/0.082), \pan, Pseq(#[1, -0.5, 1, 0.3, -0.3, 1, 0.7], inf), \vol,  (0.4 - (0.1*(cosFunc+0.3)*(0.6-cosFunc))), \envIdx, 1);
		// ------------T1Cell 3 idem que T1Cell2 autres sons-------------------------
		t1c3p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, tmax3+((minmax3)*0.9*(cosFunc+0.1)*(sinFunc+1.1)), \speed, 0.25+(0.15*(cosFunc+0.1)*(0.55+sinFunc)), \len, 0.07,\freq, fmax3, \q, 1.2,\frqEnvIdx, 1, \dur,  Pseq([0.081], t1dur3/0.081), \pan, Pseq(#[0, -1, -0.5, 0.3, 1, 0.7], inf), \vol, 0.6 -(0.18*rampFunc), \envIdx, 5);
		t1c3p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, tmax4-((minmax4)*0.8*(sinFunc-0.2)*(cosFunc+0.9)), \speed, 0.3+(0.2*(sinFunc+0.2)*(cosFunc+0.3)), \len, 0.07,\freq, fmax4, \q, 1.2,\frqEnvIdx, 1, \dur,  Pseq([0.076], t1dur3/0.076), \pan, Pseq(#[-0.5, 1, 0.3, 1, 0.7], inf), \vol,  0.6 -(0.12*rampFunc), \envIdx, 5);
		t1c3p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, tmax6+((minmax6)*0.7*(sinFunc+0.1)*(sinFunc+0.4)), \speed, 0.4+(0.3*cosFunc*(1.1-cosFunc)), \len, 0.07,\freq, fmax6, \q, 1.2,\frqEnvIdx, 1, \dur,  Pseq([0.082], t1dur3/0.082), \pan, Pseq(#[1, -0.5, 1, 0.3, -0.3, 1, 0.7], inf), \vol,  0.60-(0.15*rampFunc), \envIdx, 5);
		// ------------T1Cell 4 comme 2 et 3 son a envers et avec tmx-------------------------
		t1c4p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmx1+((minmx1)*0.9*(cosFunc+0.1)), \speed, -1.5+0.04*(cosFunc+0.1), \len, 0.09,\freq, fmx1, \q, 1,\frqEnvIdx, 1, \dur,  Pseq([0.09], t1dur4/0.09), \pan, 0.5*(1-sinFunc), \vol, (0.9/t1dur4)*rampFunc, \envIdx, 5);
		t1c4p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmx2-((minmx2)*0.8*(sinFunc-0.1)), \speed, -1.5-0.03*(cosFunc+0.2), \len, 0.1,\freq, fmx2, \q, 1,\frqEnvIdx, 1, \dur,  Pseq([0.1], t1dur4/0.1), \pan, 0.5*(1+sinFunc), \vol,  (1/t1dur4)*rampFunc, \envIdx, 5);
		t1c4p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, tmx3+((minmx3)*0.7*(cosFunc+0.1)*(sinFunc+0.2)), \speed, -1.5+0.03*(cosFunc+0.1), \len, 0.12,\freq, fmx3, \q, 1,\frqEnvIdx, 1, \dur,  Pseq([0.12], t1dur4/0.12), \pan, 0.5*(1-cosFunc), \vol,  (1.2/t1dur4)*rampFunc, \envIdx, 5);
		// ------------T1Cell 5 complementaire de cell4-------------------------
		t1c5p1= Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, tmx4+((minmx4)*0.9*(cosFunc+0.4)), \speed, 0.8+(0.01*(cosFunc+0.1)*(sinFunc-0.2)), \len, 0.04,\freq, fmx4, \q, 1,\frqEnvIdx, 1, \dur,  Pseq([0.04], t1dur5/0.04), \pan, -0.5*(1+cosFunc), \vol, (0.7-(0.05*(rampFunc))), \envIdx, 5);
		t1c5p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, tmx5-((minmx5)*0.8*(sinFunc+0.1)), \speed, 0.8-(0.03*cosFunc*(cosFunc+0.2)), \len, 0.05,\freq, fmx5, \q, 1,\frqEnvIdx, 1, \dur,  Pseq([0.05], t1dur5/0.05), \pan, -0.5*(1+sinFunc), \vol,  (0.7-(0.06*(rampFunc))), \envIdx, 5);
		t1c5p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, tmx6+((minmx6)*0.7*cosFunc*(sinFunc+0.7)), \speed, 0.8+(0.06*cosFunc), \len, 0.04,\freq, fmx6, \q, 1,\frqEnvIdx, 1, \dur,  Pseq([0.04], t1dur5/0.04), \pan, -0.5*(1-sinFunc), \vol,  (0.7-(0.08*(rampFunc))), \envIdx, 5);
		// ------------T1Cell 6-------------------------//son repete du debut
		t1c6p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (l6/2)+(0.4*cosFunc), \speed, 1.6, \len, 0.045,\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.048], t1dur6/0.048), \pan, sin(0.1*rampFunc), \vol, 0.7-(0.3*(1.2-compFunc1)*(sinFunc+0.2)), \envIdx, 1);
		// ------------T1Cell 7-------------------------// modulation en anneau aleatoire
		t1c7p1 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, (l3/2)+((l3/4)*cosFunc), \speed, 0.8, \len, 0.075, \dur, Pseq([0.08], t1dur7/0.08), \pan, 0.7*(cosFunc+sinFunc), \vol, 0.8-(0.06*rampFunc), \envIdx, 3);
		t1c7p2 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, (l1/2)+((l1/4)*cosFunc), \speed, 1.2, \len, 0.065, \dur, Pseq([0.07], t1dur7/0.07), \pan, 0.7*(cosFunc-sinFunc), \vol, 0.8-(0.05*rampFunc), \envIdx, 3);
		t1c7p3 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, (l5/2)+((l5/4)*cosFunc), \speed, 0.9, \len, 0.055, \dur, Pseq([0.06], t1dur7/0.06), \pan, -0.7*(cosFunc-sinFunc), \vol, 0.8-(0.04*rampFunc), \envIdx, 3);
		t1c7p4 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, (l4/2)+((l4/4)*cosFunc), \speed, 1.1, \len, 0.060, \dur, Pseq([0.07], t1dur7/0.07), \pan, -0.7*(cosFunc-sinFunc), \vol, 0.8-(0.065*rampFunc*(cosFunc-0.5)), \envIdx, 3);
		// ------------T1Cell 8-------------------------//montee rapide
		t1c8p1= Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, (tmx4+((minmx4)*0.9*cosFunc)), \speed, 0.4+(2*rampFunc), \len, 0.065,\freq, fmax4, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.07], t1dur8/0.07), \pan, -0.5+(sinFunc), \vol, min(0.8, (0.38/t1dur8)*(rampFunc)), \envIdx, 5);
		t1c8p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmax2-((minmax2)*0.8*(sinFunc+0.1)), \speed, 0.3+(1.5*rampFunc), \len, 0.055,\freq, fmax2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.06], t1dur8/0.06), \pan, -0.5+(sinFunc), \vol,  min(0.8, (0.38/t1dur8)*(rampFunc)), \envIdx, 5);
		t1c8p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, tmx6-((minmx6)*0.7*cosFunc*(sinFunc+0.6)), \speed, 0.3+(2*rampFunc), \len, 0.045,\freq, fmx6, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.05], t1dur8/0.05), \pan, -0.5-(sinFunc), \vol,  min(0.8, (0.38/t1dur8)*(rampFunc)), \envIdx, 5);
		t1c8p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, tmax5-((minmax5)*0.6*(sinFunc+0.2)*(cosFunc+0.6)), \speed, 0.4+(2*rampFunc), \len, 0.045,\freq, fmx6, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.05], t1dur8/0.05), \pan, -0.5-(sinFunc), \vol,  min(0.8, (0.38/t1dur8)*(rampFunc)), \envIdx, 5);
		// ------------T1Cell 9-------------------------//montee tres lente, discrete
		t1c9p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, Pseq(#[0.4, 0.2, 0.1, 0, 0.1, 0.14, 0.2], inf), \speed, (0.1 + (1.2*rampFunc)), \len, 0.05,\freq, Pseq(#[400,  500, 600, 300, 400, 600], inf), \q, 1.1,\frqEnvIdx, 2, \dur, Pseq([0.5], (t1dur9/0.5)), \pan, 0.3+(0.4*sinFunc), \vol, 0.4+(5*rampFunc/t1dur9), \envIdx, 3);
		t1c9p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, Pseq(#[0.4, 0.2, 0.1, 0, 0.1, 0.14, 0.2], inf), \speed, (0.2 + (1.3*rampFunc)), \len, 0.09,\freq, Pseq(#[400,  500, 700, 600, 300, 200, 400, 600], inf), \q, 1.1,\frqEnvIdx, 2, \dur, Pseq([0.6], (t1dur9/0.6)), \pan, 0.3+(0.4*sinFunc), \vol, 0.4+(6*rampFunc/t1dur9), \envIdx, 3);
		t1c9p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, Pseq(#[0.4, 0.2, 0.1, 0, 0.1, 0.14, 0.2], inf), \speed, (0.3 + (1.1*rampFunc)), \len, 0.08,\freq, Pseq(#[400,  500, 700, 600, 300, 400, 600], inf), \q, 1.1,\frqEnvIdx, 2, \dur, Pseq([0.55], (t1dur9/0.55)), \pan, 0.3+(0.4*sinFunc), \vol, 0.4+(5.5*rampFunc/t1dur9), \envIdx, 3);
		t1c9p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, Pseq(#[0.4, 0.2, 0.1, 0, 0.1, 0.14, 0.2], inf), \speed, (0.25 + (1.4*rampFunc)), \len, 0.07,\freq, Pseq(#[400,  500, 700, 600, 300, 400, 600], inf), \q, 1.1,\frqEnvIdx, 2, \dur, Pseq([0.45], (t1dur9/0.45)), \pan, 0.3+(0.4*sinFunc), \vol, 0.4+(4.5*rampFunc/t1dur9), \envIdx, 3);
		// ------------++T1Cell 10-------------------------// tenue cristalline symetrique de cell 6
		t1c10p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmax1*(1+(cosFunc*(1.2-sinFunc))), \speed, 1.3, \len, 0.045,\freq, Pseq(#[1400,  700, 300, 1800, 600], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.048], t1dur10/0.048), \pan, squared(cos(compFunc1)), \vol, 0.7-(0.3*sinFunc), \envIdx, 1);
		t1c10p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, tmax3*(1+((sinFunc+0.1)*(1.1-cosFunc))), \speed, 1.1, \len, 0.035,\freq, Pseq(#[1400,  200, 500, 700, 300, 1800, 600], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.038], t1dur10/0.038), \pan, squared(sin(compFunc1)), \vol, 0.7-(0.4*cosFunc), \envIdx, 1);
		t1c10p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, tmax4*(1+((sinFunc+0.1)*(1-sinFunc))), \speed, 0.9, \len, 0.040,\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.042], t1dur10/0.042), \pan, squared(sin(compFunc1)-cos(compFunc1)), \vol, 0.7+(0.5*sinFunc), \envIdx, 1);
		t1c10p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, tmax6*(1+(cosFunc*(1.1-cosFunc))), \speed, 1.2, \len, 0.050,\freq, Pseq(#[1400,  700, 300, 500, 600, 1800], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.046], t1dur10/0.046), \pan, squared(cos(compFunc1)-sin(compFunc1)), \vol, 0.7+(0.43*cosFunc), \envIdx, 1);
		// ------------T1Cell 11-------------------------// tenue lente et discrete
		t1c11p1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmin2+(tmin2*0.6*(sinFunc+0.6)*(1.1-(0.6*compFunc1))), \speed, 0.8, \len, 0.090, \dur, Pseq([0.55], t1dur11/0.55), \pan, -0.6-0.4*(cosFunc+sinFunc), \vol, 0.5-(0.5*compFunc1), \envIdx, 1);
		t1c11p2 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, tmin4+(tmin4*0.8*(cosFunc+0.4)*(1.2-compFunc1)), \speed, 0.5, \len, 0.095, \dur, Pseq([0.1], t1dur11/0.1), \pan, -0.6-0.4*(cosFunc-sinFunc), \vol, 0.5-(0.5*compFunc1), \envIdx, 1);
		t1c11p3 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, tmin3+(tmin3*0.7*(cosFunc+0.2)*(0.2+(1.4*compFunc1))), \speed, 0.9, \len, 0.090, \dur, Pseq([0.65], t1dur11/0.65), \pan, -0.6-0.4*(cosFunc-sinFunc), \vol, 0.5-(0.5*compFunc1), \envIdx, 1);
		t1c11p4 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, tmin5+(tmin5*0.5*((-1*cosFunc)+0.9)*(0.6+(0.8*compFunc1))), \speed, 0.6, \len, 0.085, \dur, Pseq([0.3], t1dur11/0.3), \pan, -0.6-0.4*(cosFunc-sinFunc), \vol, 0.5-(0.5*compFunc1), \envIdx, 1);
		// ------------T1Cell 12-------------------------// montee douce
		t1c12p1= Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, tmax3 + (cosFunc*0.09), \speed, 0.8+ 0.002*rampFunc, \len, 0.065,\freq, fmax3, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.07], t1dur12/0.07), \pan, 1-compFunc1, \vol, 0.1+((0.07*10*rampFunc)/t1dur12), \envIdx, 5);
		t1c12p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, tmx4-(cosFunc*0.08), \speed, 1.2-0.002*rampFunc, \len, 0.055,\freq, fmax4, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.06], t1dur12/0.06), \pan, 1-compFunc1, \vol,  0.1+((0.06*10*rampFunc)/t1dur12), \envIdx, 5);
		t1c12p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmx1-(0.07*sinFunc), \speed, 0.8+0.002*rampFunc, \len, 0.045,\freq, fmx1, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.05], t1dur12/0.05), \pan, 1-compFunc1, \vol,  0.1+((0.05*10*rampFunc)/t1dur12), \envIdx, 5);
		t1c12p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, tmx5+(0.01*cosFunc), \speed, 0.5 + 0.006*rampFunc, \len, 0.055,\freq, Pseq(#[1400, 700, 300, 1800, 600], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.058], t1dur12/0.058), \pan, -0.5+0.7*(1-compFunc1), \vol, 0.1+((0.58*rampFunc)/t1dur12), \envIdx, 5);
		t1c12p5 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmx2+ (0.01*cosFunc), \speed, 0.3 + (rampFunc*0.02), \len, 0.045,\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.048], t1dur12/0.048), \pan, -0.5+0.7*(1-compFunc1), \vol, 0.1+((0.49*rampFunc)/t1dur12), \envIdx, 5);
		// ------------T1Cell 13-------------------------// petit climax (citation)
		t1c13p1 = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmax1, \speed, 1,\len, t1dur13,\dur, Pseq([t1dur13], 1),\pan, -0.7,\vol, 0.3,\envIdx, 1);
		t1c13p2 = Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmax2, \speed, 1,\len, t1dur13,\dur, Pseq([t1dur13], 1),\pan, 0.5,\vol, 0.3,\envIdx, 4);
		t1c13p3 = Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmax3, \speed, 1,\len, t1dur13*(lh5/10),\dur, Pseq([ t1dur13*(lh5/10)], 1),\pan, 0,\vol, 0.3,\envIdx, 4);
		t1c13p4 = Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmax5, \speed, 1,\len, t1dur13*((10-lh5)/10),\dur, Pseq([t1dur13*((10-lh5)/10)], 1),\pan,-0.3,\vol, 0.3,\envIdx, 4);

		// ------------- play it --------------------

		// noter pour chaque cellule le temps de depart et son nom
		Ptpar([
			t1zeit1, t1c1p1, t1zeit1, t1c1p2, t1zeit1, t1c1p3, // t1cell1 at t1zeit1
			t1zeit2, t1c2p1, (t1zeit2 + 0.001), t1c2p2, (t1zeit2 + 0.002), t1c2p3, //t1cell2 at t1zeit2
			t1zeit3, t1c3p1, (t1zeit3 + 0.001), t1c3p2, (t1zeit3 + 0.002), t1c3p3, //t1cell3
			t1zeit4, t1c4p1, (t1zeit4 + 0.001), t1c4p2, (t1zeit4 + 0.002), t1c4p3,
			t1zeit5, t1c5p1, (t1zeit5 + 0.001), t1c5p2, (t1zeit5 + 0.002), t1c5p3,
			t1zeit6, t1c6p1,
			t1zeit7, t1c7p1, (t1zeit7 + 0.001), t1c7p2, (t1zeit7 + 0.002), t1c7p3, (t1zeit7 + 0.003), t1c7p4,
			t1zeit8, t1c8p1, (t1zeit8 + 0.001), t1c8p2, (t1zeit8 + 0.002), t1c8p3, (t1zeit8 + 0.003), t1c8p4,
			t1zeit9, t1c9p1, (t1zeit9 + 0.001), t1c9p2, (t1zeit9 + 0.002), t1c9p3, (t1zeit9 + 0.003), t1c9p4,
			t1zeit10, t1c10p1, (t1zeit10 + 0.001), t1c10p2, (t1zeit10 + 0.002), t1c10p3, (t1zeit10 + 0.003), t1c10p4,
			t1zeit11, t1c11p1, (t1zeit11 + 0.001), t1c11p2, (t1zeit11 + 0.002), t1c11p3, (t1zeit11 + 0.003), t1c11p4,
			(t1zeit12+0.001), t1c12p1, (t1zeit12 + 0.001), t1c12p2, (t1zeit12 + 0.002), t1c12p3, (t1zeit12 + 0.003), t1c12p4, (t1zeit12 + 0.002), t1c12p5,
			(t1zeit13 - t1dur13), t1c13p4, (t1zeit13 - ( t1dur13*(lh5/10))), t1c13p3, t1zeit13, t1c13p2, (t1zeit13 + 0.001), t1c13p1

			],  1);
		}; // END of teil1

		//****************************************************************************************
		//********************************      IV-2 SCORE FOR THE SECOND PART       ************
		//****************************************************************************************

		//Partie douce transition avant reprise plus rythmee

		//--------------Score of time (Tzeit = starting points of the cells, Tdur = duration of each)
		teil2 = {
			//------------ variables --------------------------------
			var  t2c1p1, t2c1p2, t2c1p3, t2c2p1, t2c2p2, t2c2p3, t2c3p1, t2c3p2,
			t2zeit1, t2zeit2, t2zeit3, t2zeit4, t2zeit5, t2dur1, t2dur2, t2dur3, t2dur4, t2dur5 ;

			//--------------Partition du temps--------------------------
			t2zeit1 = 0;             t2dur1 = ((l3 + l5)/2) + (l5/3)+(l4/3)+lh2+0.5+ lh4;  // TEIL II dure jusqu'au t3c3, mais TIII demarre plus tot
			t2zeit2 = t2zeit1 +1;    t2dur2 =  t2dur1 -1;
			t2zeit3 = t2zeit1;       t2dur3 = t2dur1;
			t2zeit4 = t2zeit3 + 0.9; t2dur4 = t2dur3 -0.9;
			t2zeit5 = t2zeit4 + 0.8; t2dur5 = t2dur4 -0.8;

			globalT2 = globalT1 + ((l3 + l5)/2);
			//---------------------------------------------------------------------------
			//----------------- Description of each cell of the part T2 ----------------
			//---------------------------------------------------------------------------
			// les enveloppes generateurs des pan pour mouvement continu ont ete enleves par rapport a version OS 9
			//   -------------- Cellen   --------------
			t2c1p1 = Pbind(\instrument, \instFiltr, \bufNum, soundFile4.bufnum, \start, tmin4-(0.5*cosFunc), \speed, 1.2, \freq, fmin5, \q, 1.1,\frqEnvIdx, 1, \len, (0.3 + (0.5*squared(compFunc1))), \dur, Pseq([2.01], t2dur1/2.01) , \pan, (1-sinFunc), \vol, 0.7-vmin5, \envIdx, 3);
			t2c1p2 = Pbind(\instrument, \instFiltr, \bufNum, soundFile4.bufnum, \start, tmin4+(0.4*cosFunc), \speed, 1.2, \freq, fmx3, \q, 1.1,\frqEnvIdx, 3, \len, (0.4 - (0.1*squared(compFunc1))), \dur, Pseq([2], t2dur2/2) , \pan, compFunc1, \vol, 0.8-vmin5, \envIdx, 1);

			t2c2p1 = Pbind(\instrument, \instFiltr, \bufNum, soundFile3.bufnum, \start, tmin3+(0.45*cosFunc), \speed, 1.1, \freq, fmx2, \q, 1.2,\frqEnvIdx, 1, \len, (0.4 - (0.2*squared(compFunc1))), \dur, Pseq([(3*(sqrt(fmin5/fmin3)))], (t2dur3)/(3*(sqrt(fmin5/fmin3)))) , \pan, (0.5-(sinFunc*cosFunc)), \vol, 0.9-vmin3, \envIdx, 1);
			t2c2p2 = Pbind(\instrument, \instFiltr, \bufNum, soundFile3.bufnum, \start, tmin3+(0.35*sinFunc), \speed, 1.1, \freq, fmin6, \q, 1.2,\frqEnvIdx, 3, \len, (0.4 - (0.1*cosFunc)), \dur, Pseq([(2.99*(sqrt(fmin5/fmin3)))], (t2dur4)/(2.99*(sqrt(fmin5/fmin3)))) , \pan, (sinFunc+0.6), \vol, 0.8-vmin3, \envIdx, 3);
			t2c2p3 = Pbind(\instrument, \instFiltr, \bufNum, soundFile3.bufnum, \start, tmin3-(0.42*cosFunc*sinFunc), \speed, 1.1, \freq, fmin3, \q, 1.2,\frqEnvIdx, 3, \len, (0.3 + (0.5*squared(compFunc1))), \dur, Pseq([(3.01*(sqrt(fmin5/fmin3)))], (t2dur5)/(3.01*(sqrt(fmin5/fmin3)))) , \pan, ((2*sinFunc)-3), \vol, 0.8-vmin3, \envIdx, 3);

			t2c3p1 = Pbind(\instrument, \instFiltr, \bufNum, soundFile1.bufnum, \start, tmin1+(0.63*sinFunc), \speed, 0.9, \freq, fmin2, \q, 1.1,\frqEnvIdx, 3, \len, (0.2 - (0.1*squared(compFunc1))), \dur, Pseq([(2.5*(sqrt(fmin5/fmin3)))], (t2dur1)/(2.5*(sqrt(fmin5/fmin1)))) , \pan, compFunc1, \vol, 0.5-vmin1, \envIdx, 3);
			t2c3p2 = Pbind(\instrument, \instFiltr, \bufNum, soundFile1.bufnum, \start, tmin1-(0.53*cosFunc), \speed, 0.9, \freq, fmx4, \q, 1.1,\frqEnvIdx, 3, \len, (0.2 - (0.1*squared(compFunc1))), \dur, Pseq([(2.5*(sqrt(fmin5/fmin3)))], (t2dur1)/(2.5*(sqrt(fmin5/fmin1)))) , \pan, (2*cosFunc*(1+compFunc1)), \vol, 0.5-vmin1, \envIdx, 3);

			// ------------- play it --------------------

			// noter pour chaque cellule le temps de d�part et son nom
			Ptpar([
				t2zeit1, t2c1p1, t2zeit2, t2c1p2,
				t2zeit3, t2c2p1, t2zeit4, t2c2p2, t2zeit5, t2c2p3,
				t2zeit1, t2c3p1, t2zeit1, t2c3p2
				],  1);
			}; // END of teil2


			//****************************************************************************************
			//***********   IV-2 SCORE FOR THE BEGINNING OF THE THIRD PART (TIII-1)       ************
			//****************************************************************************************

			// reprise

			//--------------Score of time (Tzeit = starting points of the cells, Tdur = duration of each)
			teil31 = {
				//------------ variables --------------------------------
				var t3c1p1, t3c1p2, t3c1p3, t3c1bp1, t3c1bp2, t3c1bp3, t3c2p1, t3c2p2, t3c2p3,
				t3c3p1, t3c3p2, t3c3p3, t3c4p1, t3c4p2, t3c4p3, t3c5p1, t3c5p2, t3c5p3, t3c5p4, t3c5p5,
				t3zeit1, t3zeit2, t3zeit3, t3zeit4, t3zeit5, t3zeit1b, t3dur1, t3dur2, t3dur3, t3dur4,     t3dur5, t3dur1b;

				//--------------Partition du temps--------------------------
				t3zeit1 = 0;
				t3zeit2 = t3zeit1 + (l5/3);        t3dur2 = lh2+0.5 + (l4/3) + lh4;
				t3zeit3 = t3zeit2 + t3dur2 - lh4;    t3dur3 = lh4+lh2+lh1+lh5;
				t3zeit4 = t3zeit3 + lh4;       t3dur4 = (lh2+lh1+lh5) +(lh6+lh5+lh4+lh3); //deborde sur partie 2 jusqu a debut t3c7
				t3zeit5 = t3zeit4 + lh2+lh1;     t3dur5 = (lh5+lh3)/3;
				t3dur1 =  t3zeit5 + t3dur5 +l3/2+(2*lh6)+lh4+(2*lh1) -t3zeit1;
				t3zeit1b = t3zeit1 + t3dur1;       t3dur1b = (l2/2); // fin de la cell1

				globalT31 = globalT2 +t3zeit5 + lh5; // Startzeit Teil3.2

				//---------------------------------------------------------------------------
				//----------------- Description of each cell of the part TIII-1 ----------------
				//---------------------------------------------------------------------------
				// ----------- T3Cell 1 (issue de t1c1)--------------
				t3c1p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmin1 + (0.12*(l1/l3)*(cosFunc + 0.1))), \speed, 5.01, \len, 0.047*(l1/l3),\freq, fmin1*(1.3+cosFunc), \q, 1.3,\frqEnvIdx, 2, \dur, Pseq([0.057*(l1/l3)], t3dur1/(0.057*(l1/l3))), \pan, compFunc1,  \vol, (1-vmin1)*(1-compFunc1) +(0.2*(1-vmin1)*cosFunc), \envIdx, 3);
				t3c1p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, (tmin3 + (0.1*(l2/l3)*(cosFunc+0.2))), \speed, 4.99*(fmin1/fmin3), \len, 0.046*(l2/l3),\freq, fmin3*(1.4+cosFunc), \q, 1.2,\frqEnvIdx, 2, \dur, Pseq([0.054*(l2/l3)], t3dur1/(0.054*(l2/l3))), \pan, compFunc1,  \vol, (1-vmin3)*(1-compFunc1) -(0.25*(1-vmin3)*cosFunc), \envIdx, 3);
				t3c1p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, (tmin5 + (0.16*(l4/l3)*(cosFunc + 0.4))), \speed, 4.98*(fmin1/fmin5), \len, 0.045*(l4/l3),\freq, fmin5*(1.2+cosFunc), \q, 1.5,\frqEnvIdx, 2, \dur, Pseq([0.048*(l4/l3)], t3dur1/(0.048*(l4/l3))), \pan, compFunc1,  \vol, (1-vmin5)*(1-compFunc1) +(0.3*(1-vmin5)*cosFunc), \envIdx, 3);
				// ----------- T3Cell 2--------------------------
				t3c2p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, 0.1 + (0.2*(l2/l5)*squared(1-compFunc1)), \speed, 1, \len, 0.04*(l2/l5),\freq, Pseq(#[400,  1500, 300, 800, 600], inf), \q, 1.3,\frqEnvIdx, 2, \dur, Pseq([0.08*(l2/l5)], t3dur2/(0.08*(l2/l5))), \pan, Pseq(#[-1, 0.3, -0.7, 1, -0.3, 0.7], inf), \vol, (0.9-(((0.8*(l2/l5))/t3dur2)*rampFunc)), \envIdx, 3);
				t3c2p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, 0.2 + (0.2*(l2/l3)*squared(1-compFunc1)), \speed, ((0.1*rampFunc)+0.1), \len, 0.05*(l2/l3),\freq, Pseq(#[400, 300, 800, 600], inf), \q, 1.3,\frqEnvIdx, 2, \dur, Pseq([0.06*(l2/l3)], t3dur2/(0.06*(l2/l3))), \pan, -0.4+(0.1*rampFunc), \vol, (0.9-(((0.6*(l2/l3))/t3dur2)*rampFunc)), \envIdx, 1);
				t3c2p3 = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin1, fmin3, fmin4], \bufNum, soundFile1.bufnum, \start, 0.2 + (0.2*(l2/l1)*squared(1-compFunc1)), \speed, ((0.4*rampFunc)+1), \len, 0.06*(l2/l1),\dur, Pseq([0.07*(l2/l1)], t3dur2/(0.07*(l2/l1))), \pan, -0.1+(0.3*rampFunc), \vol, (0.9-(((0.7*(l2/l1))/t3dur2)*rampFunc)), \envIdx, 5);
				// ----------- T3Cell 3--------------------------
				t3c3p1 = Pbind(\instrument, \instKlankd, \specf, [fmx1, fmx2, fmx3, fmx4], \bufNum, soundFile6.bufnum, \start, tmx6 + (0.2*(l6/l3)*squared(compFunc1)), \speed, ((0.3*rampFunc)+0.5), \len, 0.045*(l6/l3),\dur, Pseq([0.07*(l6/l3)], t3dur3/(0.07*(l6/l3))), \pan, 0.1-(0.1*rampFunc), \vol, (0.9-(((0.7*(l6/l3))/t3dur3)*rampFunc)), \envIdx, 5);
				t3c3p2 = Pbind(\instrument, \instKlankd, \specf, [fmx1, fmx2, fmx5, fmx6], \bufNum, soundFile4.bufnum, \start, tmx4 + (0.2*(l4/l3)*squared(compFunc1)), \speed, ((0.4*rampFunc)+0.75), \len, 0.05*(l4/l3),\dur, Pseq([0.065*(l4/l3)], t3dur3/(0.09*(l4/l3))), \pan, 0.4-(0.2*rampFunc), \vol, (0.9-(((0.9*(l4/l3))/t3dur3)*rampFunc)), \envIdx, 5);
				t3c3p3 = Pbind(\instrument, \instKlankd, \specf, [fmx3, fmx4, fmx5, fmx6], \bufNum, soundFile2.bufnum, \start, tmx2 + (0.2*(l2/l3)*squared(compFunc1)), \speed, ((0.5*rampFunc)+1), \len, 0.06*(l2/l3),\dur, Pseq([0.06*(l2/l3)], t3dur3/(0.12*(l2/l3))), \pan, -1+(0.4*rampFunc), \vol, (0.91-(((1.2*(l2/l3))/t3dur3)*rampFunc)), \envIdx, 5);
				// ----------- T3Cell 4--------------------------
				t3c4p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmax1 + (0.1*(l1/l5)*(1-compFunc1)), \speed, 1, \len, 0.06*(l1/l5),\freq, Pseq(#[400,  1500, 300, 800, 600], inf), \q, 1.3,\frqEnvIdx, 3, \dur, Pseq([0.1*(l1/l5)], t3dur4/(0.1*(l1/l5))), \pan, Pseq(#[-1, 0.3, -0.7, 1, -0.3, 0.7], inf), \vol, 0.8 + (0.5*(0.4-cosFunc)*(sinFunc+0.3)), \envIdx, 1);
				t3c4p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmax2 + (0.1*(l2/l5)*(1+cosFunc)), \speed, 1, \len, 0.06*(l2/l5),\freq, Pseq(#[400,  1500, 300, 800, 600], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.1*(l2/l5)], t3dur4/(0.1*(l2/l5))), \pan, Pseq(#[1, 0.3, 1, 0.5, -0.5, 0, -0.7, -0.3, -1, -0.5, 0.7], inf), \vol, (0.8- (0.4*(squared((cosFunc - 0.63)*(sinFunc + 0.2))))), \envIdx, 1);
				t3c4p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, tmax3 + (0.1*(l3/l5)*compFunc1), \speed, 1, \len, 0.06*(l3/l5),\freq, Pseq(#[ 300, 800, 600], inf), \q, 1.2,\frqEnvIdx, 2, \dur, Pseq([0.1*(l3/l5)], t3dur4/(0.1*(l3/l5))), \pan, Pseq(#[0, -0.5, 0.5, -1, 0.3, -0.7, 0.5, -0.3, 0.7, -1, 0.5], inf), \vol, (0.8 - (0.45*(0.8-cosFunc)*(sinFunc-0.4))), \envIdx, 1);
				// ----------- T3Cell 5 Zitat--------------------------
				t3c5p1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile2.bufnum, \bufNum2, soundFile3.bufnum, \start, (min(tmax2, tmax3) + (0.1*(l2/l3)*cosFunc)), \speed, 1+ (0.027*rampFunc), \dur, Pseq([0.3*(l2/l3)],t3dur5/(0.3*(l2/l3))), \len, 0.3*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (-0.1*lh2)+(0.7*cosFunc), \vol, (0.8-((3*(l2/l3)/t3dur5)*rampFunc)), \envIdx, 3);
				t3c5p2 = Pbind( \instrument, \instXFade, \bufNum1, soundFile4.bufnum, \bufNum2, soundFile1.bufnum, \start, (min(tmax4, tmax1) + (0.1*(l4/l1)*cosFunc)), \speed, 1+ (0.017*rampFunc), \dur, Pseq([0.3*(l4/l1)],t3dur5/(0.3*(l4/l1))), \len, 0.3*(l4/l1), \theCrossFreq, (2+(0.5*cosFunc)), \pan, ((0.1*lh4)-(0.8*sinFunc)), \vol, (0.8-((3*(l4/l1)/t3dur5)*rampFunc)), \envIdx, 3);
				t3c5p3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile5.bufnum, \start, (min(tmax1, tmax5) + (0.1*(l6/l5)*cosFunc)), \speed, 1+ (0.013*rampFunc), \dur, Pseq([0.3*(l6/l5)],t3dur5/(0.3*(l6/l5))), \len, 0.3*(l6/l5), \theCrossFreq, (1.5+(0.5*cosFunc)),  \pan, ((0.1*lh6)-(0.9*sinFunc)), \vol, (0.8-((3*(l6/l5)/t3dur5)*rampFunc)), \envIdx, 3);
				// ----------- T3Cell 1 queue (issue de t1c1)--------------
				t3c1bp1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmin1 + (0.12*(l1/l3)*(cosFunc + 0.1))), \speed, 5.01, \len, 0.047,\freq, fmin1*(1.3+cosFunc), \q, 1.3,\frqEnvIdx, 2, \dur, Pseq([0.057], t3dur1b/0.057),\pan, (-0.1)*compFunc1,  \vol, (0.7)*(1-((10*rampFunc*0.057)/t3dur1b)), \envIdx, 3);
				t3c1bp2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, (tmin3 + (0.1*(l2/l3)*(cosFunc+0.2))), \speed, 4.96*(fmin1/fmin3), \len, 0.046,\freq, fmin3*(1.4+cosFunc), \q, 1.2,\frqEnvIdx, 2, \dur, Pseq([0.054], t3dur1b/0.054), \pan, (-0.1)*compFunc1,  \vol, (0.75)*(1-((10*rampFunc*0.054)/t3dur1b)), \envIdx, 3);
				t3c1bp3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, (tmin5 + (0.16*(l4/l3)*(cosFunc + 0.4))), \speed, 5*(fmin1/fmin5), \len, 0.045,\freq, fmin5*(1.2+cosFunc), \q, 1.5,\frqEnvIdx, 2, \dur, Pseq([0.048], t3dur1b/0.048), \pan, (-0.1)*compFunc1,  \vol, (0.8)*(1-((10*rampFunc*0.048)/t3dur1b)), \envIdx, 3);

				// ------------- play it --------------------
				Ptpar([
					t3zeit1, t3c1p1, t3zeit1, t3c1p2, t3zeit1, t3c1p3,
					t3zeit2, t3c2p1, t3zeit2 + (lh2/3), t3c2p2, t3zeit2 + (lh2/1.5), t3c2p3,
					t3zeit3, t3c3p1, t3zeit3 + ((lh3)/4), t3c3p2, t3zeit3+ ((lh3)/1.7), t3c3p3, // gliss
					t3zeit4, t3c4p1, t3zeit4, t3c4p2, t3zeit4,t3c4p3,
					t3zeit5, t3c5p1, t3zeit5, t3c5p2, t3zeit5,t3c5p3,    // crossfade
					t3zeit1b, t3c1bp1, t3zeit1b, t3c1bp2, t3zeit1b, t3c1bp3 // queue de t3c1
					],  1);
				} ; // End of part 31

				//****************************************************************************************
				//***********   IV-2         SCORE PART TIII-2     (d�but rythmique) ************
				//****************************************************************************************

				// reprise

				//--------------Score of time (Tzeit = starting points of the cells, Tdur = duration of each)
				teil32 = {
					// partie + rythmee
					// ----------------  Variablen------------------------
					var    t3c6p1, t3dur6,
					t3c7p1, t3c7p2, t3c7p3, t3c7p4, t3c7p5, t3c7p6,
					t3zeit7p1, t3zeit7p2, t3zeit7p3, t3zeit7p4, t3zeit7p5, t3zeit7p6,
					t3dur7p1, t3dur7p2, t3dur7p3, t3dur7p4, t3dur7p5, t3dur7p6,
					t3c8p1, t3c8p2, t3c8p3, t3c8p4, t3c8p5, t3c8p6,
					t3zeit8p1, t3zeit8p2, t3zeit8p3, t3zeit8p4, t3zeit8p5, t3zeit8p6,
					t3dur8p1, t3dur8p2, t3dur8p3, t3dur8p4, t3dur8p5, t3dur8p6,
					t3c9p1, t3c9p2, t3c9p3, t3c9p4, t3c9p5, t3c9p6,
					t3zeit9p1, t3zeit9p2, t3zeit9p3, t3zeit9p4, t3zeit9p5, t3zeit9p6,
					t3dur9p1, t3dur9p2, t3dur9p3, t3dur9p4, t3dur9p5, t3dur9p6,
					t3c10p1, t3c10p2, t3c10p3, t3c10p4, t3c10p5, t3c10p6,
					t3zeit10p1, t3zeit10p2, t3zeit10p3, t3zeit10p4,
					t3dur10p1, t3dur10p2, t3dur10p3, t3dur10p4,
					t3c11p1, t3c11p2, t3c11p3, t3c11p4, t3c11p5, t3c11p6,
					t3zeit11p1, t3zeit11p2, t3zeit11p3, t3zeit11p4,
					t3dur11p1, t3dur11p2, t3dur11p3, t3dur11p4,

					//queue de cellules decrescendo
					t3c7p1d, t3c7p2d, t3zeit7p1d, t3zeit7p2d, t3dur7p1d, t3dur7p2d,
					t3c8p2d,t3zeit8p2d, t3dur8p2d,
					t3c9p1d, t3zeit9p1d, t3dur9p1d,
					t3c10p1d, t3zeit10p1d, t3dur10p1d,
					t3c11p2d, t3zeit11p2d, t3dur11p2d,

					plt, plm
					;

					// _______________Partitur Teil 3 2e partie _________________________
					plt = (min ((l1/l6) , (l6/l1) ) ) ;   plm = (max (plt, 0.7) ) ;


					t3dur6 = (l3/2) +((4)*((lh1)+(lh2)+(lh3)+(lh4)+(lh5)+(lh6))); // dure teil 32 et teil 33

					t3zeit7p1 = (l3/2);           t3dur7p1 = lh6+lh5+lh3+lh4; // va jusqu a Z8p2
					t3zeit7p1d = t3zeit7p1+t3dur7p1;        t3dur7p1d = lh5;
					t3zeit7p2 = t3zeit7p1+t3dur7p1;       t3dur7p2 = lh3+lh6+(2*lh5);
					t3zeit7p2d = t3zeit7p2+t3dur7p2;        t3dur7p2d = lh4;
					t3zeit7p3 = t3zeit7p2+t3dur7p2;       t3dur7p3 = lh2+(2*lh4)+lh6;
					t3zeit7p4 = t3zeit7p3+t3dur7p3;       t3dur7p4 = lh3+lh2;
					t3zeit7p5 = t3zeit7p4 +t3dur7p4;        t3dur7p5 = lh2+lh1 + (2*lh1);//grave et continue sur t3z7p6
					t3zeit7p6 = t3zeit7p5 +t3dur7p5-(2*lh1);    t3dur7p6 = 2*lh1; // duree totale 3 de tout

					t3zeit8p1 = t3zeit7p1 +lh6+lh4;       t3dur8p1 = lh1+lh4+lh3;
					t3zeit8p2 = t3zeit8p1+t3dur8p1;       t3dur8p2 = lh3+lh2+lh1; //resre jusqu a Z8p4
					t3zeit8p2d = t3zeit8p2+t3dur8p2;        t3dur8p2d = lh5;
					t3zeit8p3 = t3zeit8p2+t3dur8p2;       t3dur8p3 = lh2+lh5+lh6;
					t3zeit8p4 = t3zeit8p3+t3dur8p3;       t3dur8p4 = lh4+lh3+lh1;
					t3zeit8p5 = t3zeit8p4+t3dur8p4;       t3dur8p5 = lh5+lh2;
					t3zeit8p6 = t3zeit8p5+t3dur8p5;       t3dur8p6 = lh6+lh5;// duree totale 3 de tout

					t3zeit9p1 = t3zeit8p1+lh1;          t3dur9p1 = (2*lh3)+lh2+lh1;
					t3zeit9p1d = t3zeit9p1+t3dur9p1;        t3dur9p1d = lh1;
					t3zeit9p2 = t3zeit9p1+t3dur9p1;       t3dur9p2 = lh6+lh4;
					t3zeit9p3 = t3zeit9p2+t3dur9p2;       t3dur9p3 = lh1+lh5+lh6;
					t3zeit9p4 = t3zeit9p3+t3dur9p3;       t3dur9p4 = lh4+lh2;
					t3zeit9p5 = t3zeit9p4+t3dur9p4;       t3dur9p5 = lh2+lh3+lh5;
					t3zeit9p6 = t3zeit9p5+t3dur9p5;       t3dur9p6 = lh5; // duree totale 3 de tout

					t3zeit10p1 = t3zeit9p1+lh3+lh2;       t3dur10p1 = (2*lh2)+lh4+lh5;
					t3zeit10p1d = t3zeit10p1+t3dur10p1;     t3dur10p1d = lh1;
					t3zeit10p2 = t3zeit10p1+t3dur10p1;      t3dur10p2 = lh6+lh3;
					t3zeit10p3 = t3zeit10p2+t3dur10p2;      t3dur10p3 = lh5+lh4+lh1;
					t3zeit10p4 = t3zeit10p3+t3dur10p3;      t3dur10p4 = lh6+lh5+lh1+lh3;// duree totale 3 de tout

					t3zeit11p1 = t3zeit7p1+lh5+lh3;       t3dur11p1 = lh3+lh4+lh2;
					t3zeit11p2 = t3zeit11p1+t3dur11p1;      t3dur11p2 = (2*lh1)+lh5+lh6+lh3;
					t3zeit11p2d = t3zeit11p2 + t3dur11p2;     t3dur11p2d = lh5;
					t3zeit11p3 = t3zeit11p2+t3dur11p2;      t3dur11p3 = lh6+lh4+lh2+lh1;
					t3zeit11p4 = t3zeit11p3+t3dur11p3;      t3dur11p4 = lh6+lh5+lh4+lh2; // duree totale 3 de tout

					globalT32 = globalT31 + (l3/2) +((3)*((lh1)+(lh2)+(lh3)+(lh4)+(lh5)+(lh6))); // Startzeit Teil33

					//---------------------------------------------------------------------------
					//----------------- Description of each cell of the part TIII-2 ----------------
					//---------------------------------------------------------------------------
					// ----------- T3Cell 6-------------------------- tmin3
					t3c6p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, (tmin3 + squared((0.3*cosFunc)-(0.8*sinFunc))), \speed, 2.1, \len, 0.08*plm,\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur6/(0.1*plm)), \pan, compFunc1, \vol, (1 - vmin3 - squared((0.1*cosFunc)+(0.2*sinFunc)) + (0.3*compFunc1)), \envIdx, 1);
					// ----------- T3Cell 7--------------------------
					t3c7p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmax2+squared((0.6*cosFunc)+(0.8*sinFunc))), \speed, 0.9 -((0.1*plm/t3dur7p1)*rampFunc), \len, 0.08*plm,\freq, Pseq(#[200, 1550, 300, 800, 2500], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur7p1/(0.1*plm)), \pan, ((0.3*compFunc1)-(0.4*cosFunc)-(0.5*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (0.9 - (15*squared((0.3*cosFunc)-(0.3*sinFunc)))) - ((5*0.1*plm/t3dur7p1)*rampFunc), \envIdx, 1);
					t3c7p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmx2+squared((0.9*cosFunc)-(0.4*sinFunc))), \speed, 1, \len, 0.08*plm,\freq, Pseq(#[4300, 200, 1567, 4302, 560], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur7p2/(0.1*plm)), \pan,  ((0.5*compFunc1)-(0.4*cosFunc)-(0.3*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (0.9-((6*0.1*plm/t3dur7p2)*rampFunc) - (10*squared((0.2*cosFunc)+(0.4*sinFunc)))), \envIdx, 1);
					t3c7p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmax2-((0.7*cosFunc)+(0.8*sinFunc))), \speed, 0.8+((10*0.1*plm/t3dur7p3)*rampFunc), \len, 0.08*plm,\freq, Pseq(#[3100, 100, 200, 890], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur7p3/(0.1*plm)), \pan, -0.7+(0.3*rampFunc), \vol, (0.9 - (9*squared((0.3*cosFunc)+(0.2*sinFunc)))), \envIdx, 1);
					t3c7p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmax2+((0.5*cosFunc)-(0.9*sinFunc))), \speed, 0.8, \len, 0.08*plm,\freq, Pseq(#[1600, 400, 700, 2700, 789], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur7p4/(0.1*plm)), \pan, 0.4+((0.4*compFunc1)+(0.3*cosFunc)-(0.2*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (0.9 - (11*squared((-0.1*cosFunc)-(0.3*sinFunc)))), \envIdx, 1);
					t3c7p5 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmx2+((-0.6*cosFunc)+(0.7*sinFunc))), \speed, 0.7+((10*0.1*plm/t3dur7p5)*rampFunc), \len, 0.09*plm,\freq, Pseq(#[678, 1290, 3409, 290, 608], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur7p5/(0.2*plm)), \pan, -0.9+((0.2*compFunc1)-(0.3*cosFunc)-(0.4*sinFunc)), \vol, (0.9 - (7*squared((0.3*cosFunc)+(0.1*sinFunc)))), \envIdx, 1);
					t3c7p6 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmax2+squared((1.4*cosFunc)+(-0.7*sinFunc))), \speed, 1.1, \len, 0.08*plm,\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur7p6/(0.1*plm)), \pan, 0.4-(0.3*rampFunc), \vol, (0.9 - (12*squared((0.1*cosFunc)-(0.3*sinFunc)))), \envIdx, 1);

					t3c7p1d = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmax2+squared((0.6*cosFunc)+(0.8*sinFunc))), \speed, 0.9, \len, 0.08*plm,\freq, Pseq(#[200, 1550, 300, 800, 2500], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur7p1d/(0.1*plm)), \pan, ((0.3*compFunc1)-(0.4*cosFunc)-(0.5*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (0.9 - ((1*plm/t3dur7p1d)*rampFunc)), \envIdx, 1);
					t3c7p2d = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmx2+squared((0.9*cosFunc)-(0.4*sinFunc))), \speed, 1, \len, 0.08*plm,\freq, Pseq(#[4300, 200, 1567, 4302, 560], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur7p2d/(0.1*plm)), \pan,  ((0.5*compFunc1)-(0.4*cosFunc)-(0.3*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (0.9-((1*plm/t3dur7p2d)*rampFunc)), \envIdx, 1);
					// ----------- T3Cell 8--------------------------
					t3c8p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmx1 + squared((0.4*sinFunc)-(0.8*cosFunc))), \speed, 1.1+((5*0.1*plm/t3dur8p1)*rampFunc), \len, 0.08*plm,\freq, Pseq(#[3100, 100, 200, 890], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur8p1/(0.1*plm)),\pan, -0.3*rampFunc, \vol, (1 - (12*squared((0.9*cosFunc)+(0.4*sinFunc)))- ((plm/t3dur8p1)*rampFunc)), \envIdx, 1);
					t3c8p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmx1 +((0.8*sinFunc)+(0.7*cosFunc))), \speed, 1.2, \len, 0.08*plm,\freq, Pseq(#[1600, 400, 700, 2700, 789], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur8p2/(0.2*plm)), \pan, -0.7+((0.2*compFunc1)-(0.3*cosFunc)+(0.4*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (((8*0.1*plm/t3dur8p2)*rampFunc) -((0.8*cosFunc)+(0.3*sinFunc))), \envIdx, 1);
					t3c8p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmax1 +((-0.8*sinFunc)+(0.6*cosFunc))), \speed, 0.9, \len, 0.08*plm,\freq, Pseq(#[678, 1290, 3409, 290, 608], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur8p3/(0.1*plm)),\pan, 0.6-(0.3*rampFunc), \vol, (1 - (6*squared((0.3*cosFunc)+(0.1*sinFunc)))), \envIdx, 1);
					t3c8p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmax1 + ((0.6*sinFunc)+(0.9*cosFunc))), \speed, 1.5-((7*0.1*plm/t3dur8p4)*rampFunc), \len, 0.09*plm,\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.4,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur8p4/(0.1*plm)), \pan, -0.4+((0.6*compFunc1)+(0.3*cosFunc)-(0.5*sinFunc)), \vol, (0.7 -((0.7*cosFunc)-(0.75*sinFunc))), \envIdx, 1);
					t3c8p5 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmx1 + squared((-0.83*sinFunc)+(0.85*cosFunc))), \speed, 1, \len, 0.08*plm,\freq, Pseq(#[4300, 200, 1567, 4302, 560], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur8p5/(0.2*plm)), \pan, -0.9+((0.5*compFunc1)-(0.4*cosFunc)), \vol, (0.8 -((0.4*cosFunc)-(0.6*sinFunc))), \envIdx, 1);
					t3c8p6 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmx1 +((0.94*sinFunc)+(0.765*cosFunc))), \speed, 1.1, \len, 0.09*plm,\freq, Pseq(#[200, 1550, 300, 800, 2500], inf), \q, 1.2,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur8p6/(0.2*plm)), \pan, -0.8+(0.3*rampFunc), \vol, (1 - (9*squared((0.1*cosFunc)+(0.3*sinFunc)))), \envIdx, 1);
					t3c8p2d = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmx1 +((0.8*sinFunc)+(0.7*cosFunc))), \speed, 1.2, \len, 0.08*plm,\freq, Pseq(#[1600, 400, 700, 2700, 789], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur8p2d/(0.2*plm)), \pan, -0.7+((0.2*compFunc1)-(0.3*cosFunc)+(0.4*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (0.75-((1.5*plm/t3dur8p2d)*rampFunc)), \envIdx, 1);
					// ----------- T3Cell 9--------------------------
					t3c9p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (tmax6 + squared((0.73*cosFunc)+(0.84*sinFunc))), \speed, 0.9, \len, 0.08*plm,\freq, Pseq(#[678, 1290, 3409, 290, 608], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur9p1/(0.2*plm)), \pan, 0.8-(0.3*rampFunc), \vol, (0.5 - (7*squared((0.83*sinFunc)+(0.72*cosFunc)))+ ((plm/t3dur9p1)*rampFunc)), \envIdx, 1);
					t3c9p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (tmx6 + ((0.65*cosFunc)+(0.76*sinFunc))), \speed, 1+((7*0.1*plm/t3dur9p2)*rampFunc), \len, 0.08*plm,\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.4,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur9p2/(0.1*plm)), \pan, 0.7+((0.6*compFunc1)+(0.2*cosFunc)+(0.3*sinFunc)), \vol, (1 - (2*squared((0.5*sinFunc)+(0.4*cosFunc))) -((0.3*plm/t3dur9p2)*rampFunc)), \envIdx, 1);
					t3c9p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (tmax6 +((0.64*cosFunc)-(0.95*sinFunc))), \speed, 1.3, \len, 0.08*plm,\freq, Pseq(#[4300, 200, 1567, 4302, 560], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur9p3/(0.1*plm)), \pan, -0.5-(0.2*rampFunc), \vol, (1 - (3*squared((0.2*sinFunc)-(0.2*cosFunc)))), \envIdx, 1);
					t3c9p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (tmx6 + squared((-0.73*cosFunc)-(0.86*sinFunc))), \speed, 1.2, \len, 0.095*plm,\freq, Pseq(#[200, 1550, 300, 800, 2500], inf), \q, 1.4,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur9p4/(0.1*plm)), \pan, 0.1+((0.3*compFunc1)-(0.2*cosFunc)+(0.1*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (1 - (4*squared((-0.3*sinFunc)-(0.4*cosFunc)))), \envIdx, 1);
					t3c9p5 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (tmax6 +((-0.87*cosFunc)+(0.54*sinFunc))), \speed, 1.1, \len, 0.09*plm,\freq, Pseq(#[1600, 400, 700, 2700, 789], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur9p5/(0.2*plm)), \pan, -0.4+((0.5*compFunc1)-(0.6*cosFunc)), \vol, (0.2+((8*0.1*plm/t3dur9p5)*rampFunc) -((0.2*sinFunc)+(0.3*cosFunc))), \envIdx, 1);
					t3c9p6 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (tmx6 + squared((0.876*cosFunc)+(0.94*sinFunc*cosFunc))), \speed, 0.7+((10*0.1*plm/t3dur9p6)*rampFunc), \len, 0.08*plm,\freq, Pseq(#[3100, 100, 200, 890], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur9p6/(0.2*plm)), \pan, 1-(0.4*rampFunc), \vol, (1- (4.5*squared((0.2*sinFunc)-(0.3*cosFunc)))), \envIdx, 1);
					t3c9p1d = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (tmax6 + squared((0.73*cosFunc)+(0.84*sinFunc))), \speed, 0.9, \len, 0.08*plm,\freq, Pseq(#[678, 1290, 3409, 290, 608], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur9p1d/(0.2*plm)), \pan, 0.8-(0.3*rampFunc), \vol, (0.5-((1*plm/t3dur9p1d)*rampFunc)), \envIdx, 1);
					// ----------- T3Cell 10--------------------------
					t3c10p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, (tmx5 + squared((1.3*sinFunc)+(0.85*cosFunc))), \speed, 1.15, \len, 0.08*plm,\freq, Pseq(#[200, 1550, 300, 800, 2500], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur10p1/(0.2*plm)), \pan, -0.5+((0.1*compFunc1)+(0.1*cosFunc)-(0.1*sinFunc)), \vol, (1 - squared((0.2*cosFunc)+(0.3*sinFunc))), \envIdx, 1);
					t3c10p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, (tmax5 + squared((0.7*sinFunc)-(0.95*cosFunc))), \speed, 1.2-((3*0.1*plm/t3dur10p2)*rampFunc), \len, 0.09*plm,\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur10p2/(0.1*plm)), \pan, 0.8-(0.4*rampFunc), \vol, (1- (4*squared((0.2*sinFunc)-(0.3*cosFunc)))), \envIdx, 1);
					t3c10p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, (tmx5 +((-0.93*sinFunc)+(0.86*cosFunc))), \speed, 1.1, \len, 0.08*plm,\freq, Pseq(#[3100, 100, 200, 890], inf), \q, 1.4,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur10p3/(0.2*plm)), \pan, ((-0.3*compFunc1)+(0.1*cosFunc)+(-0.7*sinFunc)), \vol, (0.1+((8*0.1*plm/t3dur10p3)*rampFunc)  - (5*squared((0.92*cosFunc)+(0.7*compFunc1)))), \envIdx, 1);
					t3c10p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, (tmax5 + squared((0.7*sinFunc)-(0.84*cosFunc))), \speed, 1.3, \len, (0.07*plm),\freq, Pseq(#[678, 1290, 3409, 290, 608], inf), \q, 1.2,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], (t3dur10p4/(0.1*plm))), \pan, -0.3+(0.3*rampFunc), \vol, (1 - (5.5*squared((0.2*cosFunc)-(0.3*compFunc1)))), \envIdx, 1);
					t3c10p1d = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, (tmx5 + squared((1.3*sinFunc)+(0.85*cosFunc))), \speed, 1.15, \len, 0.08*plm,\freq, Pseq(#[200, 1550, 300, 800, 2500], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur10p1d/(0.2*plm)), \pan, -0.5+((0.1*compFunc1)+(0.1*cosFunc)-(0.1*sinFunc)), \vol, (0.5-((1*plm/t3dur10p1d)*rampFunc)), \envIdx, 1);
					// ----------- T3Cell 11--------------------------
					t3c11p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, (tmax4 + ((0.6*sinFunc)+(-0.74*cosFunc))), \speed, 1.15-((2*0.1*plm/t3dur11p1)*rampFunc), \len, 0.08*plm,\freq, Pseq(#[3100, 100, 200, 890], inf), \q, 1.4,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur11p1/(0.1*plm)),  \pan, 0.1+(0.4*rampFunc), \vol, (1 - squared((0.1*cosFunc)+(0.4*sinFunc)) - ((0.5*plm/t3dur11p1)*rampFunc)), \envIdx, 1);
					t3c11p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, (tmax4 + squared((0.64*sinFunc)+(0.5*cosFunc))), \speed, 1.2, \len, 0.08*plm,\freq, Pseq(#[678, 1290, 3409, 290, 608], inf), \q, 1.2,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], t3dur11p2/(0.1*plm)), \pan, 1-(0.2*rampFunc), \vol, (1 - (3.5*squared((0.2*sinFunc)-(0.2*cosFunc)))), \envIdx, 1);
					t3c11p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, (tmx4 + ((-0.5*sinFunc)+(0.83*cosFunc))), \speed, 1+((2*0.1*plm/t3dur11p3)*rampFunc), \len, 0.08*plm,\freq, Pseq(#[4300, 200, 1567, 4302, 560], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur11p3/(0.2*plm)), \pan, -0.3+((-0.6*compFunc1)+(0.5*cosFunc)), \vol, (1 - squared((0.2*cosFunc)-(0.1*compFunc1))), \envIdx, 1);
					t3c11p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, (tmx4 + ((0.93*sinFunc)-(0.76*cosFunc))), \speed, 1.1, \len, 0.09*plm,\freq, Pseq(#[1400,  500, 3100, 100, 200, 890, 600], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.2*plm], t3dur11p4/(0.2*plm)), \pan, 0.1+(0.3*rampFunc), \vol, (1 - squared((0.5*cosFunc)-(0.1*compFunc1))), \envIdx, 1);
					t3c11p2d = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, (tmax4 + squared((0.64*sinFunc)+(0.5*cosFunc))), \speed, 1.2, \len, 0.08*plm,\freq, Pseq(#[678, 1290, 3409, 290, 608], inf), \q, 1.2,\frqEnvIdx, 1, \dur, Pseq([0.1*plm], (t3dur11p2d/(0.1*plm))), \pan, (1-(0.2*rampFunc)), \vol, (0.7-((0.7*plm/t3dur11p2d)*rampFunc)), \envIdx, 1);
					//-------------PARTITUR --------------

					Ptpar( [
						0.00, t3c6p1,
						t3zeit7p1, t3c7p1, t3zeit7p2, t3c7p2, t3zeit7p3, t3c7p3, t3zeit7p4, t3c7p4,
						t3zeit7p5, t3c7p5, t3zeit7p6, t3c7p6,
						t3zeit7p1d, t3c7p1d, t3zeit7p2d, t3c7p2d,
						t3zeit8p1, t3c8p1, t3zeit8p2, t3c8p2, t3zeit8p3, t3c8p3, t3zeit8p4, t3c8p4,
						t3zeit8p5, t3c8p5, t3zeit8p6, t3c8p6, t3zeit8p2d, t3c8p2d,
						t3zeit9p1, t3c9p1, t3zeit9p2, t3c9p2, t3zeit9p3, t3c9p3, t3zeit9p4, t3c9p4,
						t3zeit9p5, t3c9p5, t3zeit9p6, t3c9p6, t3zeit9p1d, t3c9p1d,
						t3zeit10p1, t3c10p1, t3zeit10p2, t3c10p2, t3zeit10p3, t3c10p3, t3zeit10p4, t3c10p4, t3zeit10p1d, t3c10p1d,
						t3zeit11p1, t3c11p1, t3zeit11p2, t3c11p2, t3zeit11p3, t3c11p3, t3zeit11p4, t3c11p4, t3zeit11p2d, t3c11p2d
						], 1) ;
					} ; // End of part 32

					//*********************************************************************************************************
					//*********************************************  PART III 3e **********************************************
					//*********************************************************************************************************

					teil33 = {
						// partie + rythmee
						// ----------------  Variablen------------------------
						var    t3c12p1, t3c12p2, t3c12p3, t3c12p4, t3c12p5, t3c12p6,
						t3zeit12, t3dur12,
						t3c13p1,t3c13p2, t3c13p3, t3c13p4, t3c13p5, t3c13p6,
						t3zeit13, t3dur13,
						t3c14p1, t3c14p2, t3c14p3, t3c14p4, t3c14p5, t3c14p6, t3c14p7, t3c14p8, t3c14p9,
						t3zeit14, t3dur14, t3dur14d,
						t3c15p1, t3c15p2, t3c15p3, t3c15p4,
						coeff, coeff2, t3zeit15, t3dur15
						;

						t3zeit12 = 0;
						t3dur12 = (lh1+lh2+lh3+lh4+lh5+lh6) + 0.1;

						t3zeit13 = ((2/5)*(lh1+lh2+lh3+lh4+lh5+lh6));
						t3dur13 = ((3/5)*(lh1+lh2+lh3+lh4+lh5+lh6)) + 0.1;

						t3zeit14 = (lh1+lh2+lh3+lh4+lh5+lh6);
						t3dur14 = (2*(min ((lh1/lh2), (lh2/lh1)))); t3dur14d = (2*(min ((lh3/lh4), (lh4/lh3))));

						coeff = min((max ((l3/l2), (l2/l3))), 1.5); coeff2 = max((min ((l3/l2), (l2/l3))), 0.5);
						t3zeit15 = (lh1+lh2+lh3+lh4+lh5+lh6);
						t3dur15 = 2; //petite desinence descente canon3 partie 4- dure 4 secondes

						globalT33 = (globalT32 + ((lh1)+(lh2)+(lh3)+(lh4)+(lh5)+(lh6))+ 6);  // Startzeit Teil4


						// ----------- T3Cell 12-------------------------- // commence quand les autres ont fini
						t3c12p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmax2 + squared((0.9*cosFunc)+(0.8*sinFunc))), \speed, 0.7 + (0.005*(l3-l2)*(l3/l2)*rampFunc), \len, 0.08*(l3/l5),\freq, Pseq(#[678, 1290, 3409, 290, 608], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*(l3/l5)], t3dur12/(0.1*(l3/l5))), \pan, -0.3+(-0.6*compFunc1)+(0.5*cosFunc), \vol, (0.9 - (10*squared((0.6*sinFunc)+(0.5*cosFunc)))), \envIdx, 1);
						t3c12p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, (tmx4 +((0.5*cosFunc)+(-0.8*sinFunc))), \speed, 1.1 - (0.004*(l3-l2)*(l3/l2)*rampFunc), \len, 0.18*(l3/l5),\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.4,\frqEnvIdx, 1, \dur, Pseq([0.2*(l3/l5)], t3dur12/(0.2*(l3/l5))), \pan, 0.2+((0.4*compFunc1)+(0.6*cosFunc)-(0.4*sinFunc)), \vol, (0.9 - (7*squared((0.5*sinFunc)+(0.6*cosFunc)))), \envIdx, 1);
						t3c12p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmx1 +((0.9*cosFunc)-(0.5*sinFunc))), \speed, 1.1 + (0.006*(l3-l2)*(l3/l2)*rampFunc), \len, 0.18*(l3/l5),\freq, Pseq(#[4300, 200, 1567, 4302, 560], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.2*(l3/l5)], t3dur12/(0.2*(l3/l5))), \pan, -0.2+((-0.5*compFunc1)+(0.3*cosFunc)-(0.6*sinFunc)), \vol, (0.9 - (8*squared((0.5*sinFunc)-(0.7*cosFunc)))), \envIdx, 1);
						t3c12p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, (tmax5 + ((-0.8*cosFunc)-(0.7*sinFunc))), \speed, 1.3 - (0.004*(l3-l2)*(l3/l2)*rampFunc), \len, 0.08*(l3/l5),\freq, Pseq(#[200, 1550, 300, 800, 2500], inf), \q, 1.4,\frqEnvIdx, 1, \dur, Pseq([0.1*(l3/l5)], t3dur12/(0.1*(l3/l5))), \pan, 0.1+((0.3*compFunc1)-(0.2*cosFunc)+(0.3*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (0.9 - (10*squared((-0.5*sinFunc)-(0.7*cosFunc)))), \envIdx, 1);
						t3c12p5 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (tmx6 +((-0.8*cosFunc)+(0.6*sinFunc))), \speed, 0.7 + (0.0035*(l3-l2)*(l3/l2)*rampFunc), \len, 0.08*(l3/l5),\freq, Pseq(#[1600, 400, 700, 2700, 789], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.1*(l3/l5)], t3dur12/(0.1*(l3/l5))), \pan, -0.4+((0.6*compFunc1)-(0.6*cosFunc)), \vol, (0.9 - (9*squared((0.6*sinFunc)+(0.5*cosFunc)))), \envIdx, 1);
						t3c12p6 = Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, (tmax3 +((-0.7*cosFunc)+(0.6*sinFunc))), \speed, 1 - (0.007*(l3-l2)*(l3/l2)*rampFunc), \len, 0.15*(l3/l5),\freq, Pseq(#[3100, 100, 200, 890], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.2*(l3/l5)], t3dur12/(0.2*(l3/l5))), \pan, -0.3+((-0.4*compFunc1)+(0.6*cosFunc)-(0.5*sinFunc)), \vol, (0.9 - (12*squared((0.5*sinFunc)-(0.7*cosFunc)))), \envIdx, 1);
						// ----------- T3Cell 13--------------------------
						t3c13p1 = Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, (tmx4 + squared((-0.6*cosFunc)+(0.8*sinFunc))), \speed, 1 + (0.004*(l5-l4)*(l5/l4)*rampFunc), \len, 0.09*(l2/l4),\freq, Pseq(#[678, 1290, 3409, 290, 608], inf), \q, 1.5,\frqEnvIdx, 1, \dur, Pseq([0.1*(l2/l4)], t3dur13/(0.1*(l2/l4))), \pan, -0.3+(0.6*compFunc1)-(0.5*cosFunc), \vol, (0.9 - (3*squared((0.6*sinFunc)+(-0.5*cosFunc))) + ((8/5)*((l2/l4)/t3dur13)*rampFunc)), \envIdx, 1);
						t3c13p2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, (tmax6 -((0.5*cosFunc)+(-0.8*sinFunc))), \speed, 1.1 - (0.005*(l5-l4)*(l5/l4)*rampFunc), \len, 0.19*(l2/l4),\freq, Pseq(#[1400,  500, 700, 300, 1800, 600], inf), \q, 1.4,\frqEnvIdx, 1, \dur, Pseq([0.2*(l2/l4)], t3dur13/(0.2*(l2/l4))), \pan, 0.2+((-0.4*compFunc1)+(0.6*cosFunc)+(0.4*sinFunc)), \vol, (0.9 - (9*squared((0.5*sinFunc)+(0.6*cosFunc))) + ((8/5)*((l2/l4)/t3dur13)*rampFunc)), \envIdx, 1);
						t3c13p3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, (tmax2 +((-0.7*cosFunc)-(0.5*sinFunc))), \speed, 1.1 +(0.0032*(l5-l4)*(l5/l4)*rampFunc), \len, 0.19*(l2/l4),\freq, Pseq(#[4300, 200, 1567, 4302, 560], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.2*(l2/l4)], t3dur13/(0.2*(l2/l4))),\pan, -0.2+((-0.5*compFunc1)-(0.3*cosFunc)-(0.6*sinFunc)), \vol, (0.9 - (11*squared((0.5*sinFunc)-(0.7*cosFunc))) + ((8/5)*((l2/l4)/t3dur13)*rampFunc)), \envIdx, 1);
						t3c13p4 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, (tmx1 + ((0.6*cosFunc)+(0.7*sinFunc))), \speed, 1.2 - (0.0043*(l5-l4)*(l5/l4)*rampFunc), \len, 0.09*(l2/l4),\freq, Pseq(#[200, 1550, 300, 800, 2500], inf), \q, 1.4,\frqEnvIdx, 1, \dur, Pseq([0.1*(l2/l4)], t3dur13/(0.1*(l2/l4))), \pan, 0.1+((-0.3*compFunc1)+(0.2*cosFunc)+(0.3*sinFunc))*(1-(1/(1+(3*rampFunc)))), \vol, (0.9 - (9.5*squared((-0.5*sinFunc)-(0.7*cosFunc))) + ((8/5)*((l2/l4)/t3dur13)*rampFunc)), \envIdx, 1);
						t3c13p5 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, (tmx5 +((0.8*cosFunc)+(-0.6*sinFunc))), \speed, 1.1 + (0.0017*(l5-l4)*(l5/l4)*rampFunc), \len, 0.19*(l2/l4),\freq, Pseq(#[1600, 400, 700, 2700, 789], inf), \q, 1.3,\frqEnvIdx, 1, \dur, Pseq([0.3*(l2/l4)], t3dur13/(0.3*(l2/l4))), \pan, -0.4+((-0.6*compFunc1)-(0.6*cosFunc)), \vol, (0.9 - (5*squared((0.6*sinFunc)+(0.5*cosFunc))) + ((12/5)*((l2/l4)/t3dur13)*rampFunc)), \envIdx, 1);
						// ------------T3Cell 14------------------------- petit climax (citation)
						t3c14p1 = Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, tmx6, \speed, 1,\len, t3dur14,\dur, Pseq([t3dur14], 1),\pan, -0.8,\vol, 0.9,\envIdx, 4);
						t3c14p2 = Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmx4, \speed, 1,\len, t3dur14,\dur, Pseq([t3dur14], 1),\pan, 1,\vol, 1,\envIdx, 4);
						t3c14p3 = Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmx5, \speed, 1,\len, t3dur14*(lh3/10),\dur, Pseq([t3dur14*(lh3/10)], 1),\pan, 0.8,\vol, 0.9,\envIdx, 2);
						t3c14p4 = Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmx2, \speed, 1,\len, t3dur14*((10-lh3)/10),\dur, Pseq([t3dur14*((10-lh3)/10)], 1),\pan, -1,\vol, 0.9,\envIdx, 3);
						t3c14p5 = Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmx3, \speed, 2,\len, t3dur14/2,\dur, Pseq([ t3dur14/2], 1),\pan, -0.9, \vol, 0.9,\envIdx, 4);
						t3c14p6 = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmx1, \speed, 0.8,\len, t3dur14/2,\dur, Pseq([t3dur14/2], 1),\pan, 0.9, \vol, 0.9,\envIdx, 4);
						t3c14p7 = Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmx4, \speed, 0.9,\len, t3dur14d,\dur, Pseq([t3dur14d], 1),\pan, 0.5,\vol, 0.9,\envIdx, 1);
						t3c14p8 = Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmx2, \speed, 1.1,\len, t3dur14d*((10-lh4)/10),\dur, Pseq([t3dur14d*((10-lh4)/10)], 1),\pan, -0.7,\vol, 0.9,\envIdx, 4);
						t3c14p9 = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmx1, \speed, 1,\len, 2*t3dur14d/3,\dur, Pseq([2*t3dur14d/3], 1),\pan, 0.4, \vol, 0.9,\envIdx, 1);

						//----------------T3Cell15 DESINENCE------
						t3c15p1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmax1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff)/t3dur15))), \dur, Pseq([0.03*coeff], t3dur15/(0.03*coeff)), \pan, ((18*rampFunc*0.03*coeff)/t3dur15)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);
						t3c15p2 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmax1 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 2.5+(0.01*sinFunc)+(0.01*cosFunc), \len, 0.055*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff)/t3dur15))), \dur, Pseq([0.027*coeff], t3dur15/(0.027*coeff)), \pan, ((18*rampFunc*0.025*coeff)/t3dur15)-0.8+(-0.1*cosFunc), \vol, 1, \envIdx, 1);
						t3c15p3 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmax1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (2.5-((20*rampFunc*0.035*coeff2)/t3dur15)+(0.01*sinFunc)-(0.01*cosFunc)), \len, 0.075*(coeff2+((coeff-coeff2)*((10*rampFunc*0.035*coeff2)/t3dur15))), \dur, Pseq([0.035*coeff2], t3dur15/(0.035*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.035*coeff2)/t3dur15)), \envIdx, 1);
						t3c15p4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmax1 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, (2.5-((27*rampFunc*0.032*coeff2)/t3dur15)+(0.01*sinFunc)+(0.01*cosFunc)), \len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.032*coeff2)/t3dur15))), \dur, Pseq([0.032*coeff2], t3dur15/(0.032*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.032*coeff2)/t3dur15)), \envIdx, 1);

						//-------------PARTITUR --------------
						Ptpar( [
							t3zeit12, t3c12p1, t3zeit12, t3c12p2, t3zeit12, t3c12p3,
							t3zeit12, t3c12p4, t3zeit12, t3c12p5, t3zeit12, t3c12p6,
							t3zeit13, t3c13p1, t3zeit13, t3c13p2, t3zeit13, t3c13p3, t3zeit13, t3c13p4, t3zeit13, t3c13p5,
							(t3zeit14-(t3dur14*(lh3/5))), t3c14p4,  (t3zeit14 - ( t3dur14*(lh3/10))), t3c14p3,
							t3zeit14, t3c14p1, t3zeit14, t3c14p2, (t3zeit14+(t3dur14/3)), t3c14p5, (t3zeit14+(t3dur14/3)), t3c14p6,
							(t3zeit14+t3dur14*(min((l1/l6),(l6/l1)))), t3c14p8, (t3zeit14+((2/3)*t3dur14)), t3c14p7,
							(t3zeit14+((2/3)*t3dur14)), t3c14p9,
							t3zeit15, t3c15p1, t3zeit15, t3c15p2, (t3zeit15+t3dur15), t3c15p3, (t3zeit15+t3dur15), t3c15p4
							],  1);
						} ; // End of Part III-3

						//****************************************************************************************
						//***********   IV SCORE FOR THE PART IV (with Vuza Canon)                    ************
						//****************************************************************************************

						teil4 = {
							// ---------------- Variablen------------------------
							var t4c1p1, t4c1p2, t4c1p3, t4c1p4, t4c1p5, t4c1p6,
							t4c2p1, t4c2p2, t4c2p3, t4c2p4, t4c2p5, t4c2p6,
							t4c3p1, t4c3p2, t4c3p3, t4c3p4, t4c3p5, t4c3p6,
							t4c1p1d, t4c1p2d, t4c1p3d, t4c1p4d, t4c1p5d, t4c1p6d,
							t4c2p1d, t4c2p2d, t4c2p3d, t4c2p4d, t4c2p5d, t4c2p6d,
							t4c3p1d, t4c3p2d, t4c3p3d, t4c3p4d, t4c3p5d, t4c3p6d,
							t4dur, t4durd,
							t4zeit1, t4zeit2, t4zeit3, t4zeit4, t4zeit5, t4zeit6, t4zeit7,
							t4zeit8, t4zeit9, t4zeit10, t4zeit11, t4zeit12, t4zeit13,
							puls1, puls2, puls3, puls4, puls5, puls6,
							puls, pulsd, pulsx, pulsz,
							canon1, canon2, canon3, canon4, conclu,
							canzeit1, canzeit2, canzeit3, canzeit4, conzeit;

							puls1 = (1.25*(max(min((sqrt(fmax2/fmax5)), 3), 0.1)))  + 4.75;
							// jan 2009 10 remplace par 3 dans max des puls
							// pulsations utilisees dans canons entre 2*[0.1 - 3]+2

							puls2 = (1.25*(max(min((sqrt(fmax5/fmax2)), 3), 0.1)))  + 4.75;
							// pulsations utilisees dans canons entre 2*[0.1 - 3]+2

							puls3 = (1.25*(max(min((sqrt(fmax2/fmax4)), 3), 0.1)))  + 4.75;
							// pulsations utilisees dans canons entre 1.5*[0.1 - 3]+2

							puls4 = (1.25*(max(min((sqrt(fmax1/fmax2)), 3), 0.1)))  + 4.75;
							puls5 = (1.25*(max(min((sqrt(fmax6/fmax3)), 3), 0.1)))  + 4.75;
							puls6 = (1.25*(max(min((sqrt(fmax5/fmax6)), 3), 0.1)))  + 4.75;

							puls = min((min(puls1, puls2)), 3);
							// duree de temps entre chaque point canon1 entre [2.5' - 10']
							pulsd = max(min(min ((max (((puls1)/2), ((puls2)/2))), (min (((2/3)*puls1), ((2/3)*puls2)))), 8), 1);
							// canon 2 entre [1' - 8']

							pulsx = max(min(min(((puls2)/2), ((puls3)/2)), 6), 0.5); // canon 3 entre [0.5' - 6']
							pulsz = min(max((min(((puls2)/4), ((puls3)/4))), (min(((puls1)/4), ((puls2)/4)))), 1.9); // canon 4

							t4zeit1 = 0;         // temps du cantus firmus
							t4zeit2 = t4zeit1 + (lh1+lh3+lh5);
							t4zeit3 = t4zeit1 + (lh4+lh2+lh6);
							t4zeit4 = t4zeit3 + (lh5/20);
							t4zeit5 = t4zeit1 + (lh4+lh2+lh6+lh3); // demarrage de la derniere cellule du cantus firmus

							t4zeit6 = t4zeit1 + (lh4+lh2+lh6+lh3+(lh1/3)); //pour cellule utilisant son 4
							t4zeit7 = t4zeit1 + (lh4+lh2+lh6+lh3+(lh5/2));
							t4zeit8 = t4zeit1 + (lh4+lh2+lh6+lh3+lh5+lh1);

							canzeit1= min((abs(l2/1.5)*puls), 90);
							// temps depart des differents canons de Vuza - canon1 [depart < 1'30'']

							canzeit2 = canzeit1 + ((16+((l2)/3))*puls)+0.01; // duree canon 1 : ((16+((l2)/3))*puls)
							canzeit3 = canzeit2 + ((8 + ((10*l2)/27))*pulsd)+0.01; // depart canon 3 (duree canon 2 (17/2 + l2/3)*pulsd)
							canzeit4 = canzeit3 + (29*pulsx)+0.01; // 1 puls pause avant debut canon 4 (duree canon 3 = 29 pulsx)
							t4durd = (lh1+lh2+lh3)/3 ; // valeur duree de la desinence du cantus firmus
							t4dur = canzeit4 - t4durd - t4zeit5 ; //dur�e du cantus firmus
							conzeit = canzeit4 + (189*pulsz); //debut de la conclusion 100 puls + 74 (voix 8 sauf g) + 2 silences + 5 reprise + 9 desinence - 1 commence avant fin d�sinence

							globalT4 = globalT33 + conzeit + ((l1+l2+l3+l4+l5+l6)/2); // oder so aehnlich


							fulltexttime = "[Partie 33]: " ++ ((globalT32/60).floor) ++ "'" ++ ((globalT32-(((globalT32/60).floor)*60)).floor)++ "s"++ " " ++
							"[Partie 4]: " ++ ((globalT33/60).floor) ++ ":" ++ ((globalT33-(((globalT33/60).floor)*60)).floor)++ "s"++ "\n" ++
							"[Canon 1]: " ++ (((globalT33+canzeit1)/60).floor) ++ "'" ++ (((globalT33+canzeit1)-((((globalT33+canzeit1)/60).floor)*60)).floor)++ "s"++ " " ++
							"[Canon 2]: " ++ (((globalT33+canzeit2)/60).floor) ++ "'" ++ (((globalT33+canzeit2)-((((globalT33+canzeit2)/60).floor)*60)).floor)++ "s"++ " " ++
							"[Canon 3]: " ++ (((globalT33+canzeit3)/60).floor) ++ "'" ++ (((globalT33+canzeit3)-((((globalT33+canzeit3)/60).floor)*60)).floor)++ "s"++ " " ++
							"[Canon 4]: " ++ (((globalT33+canzeit4)/60).floor) ++ "'" ++ (((globalT33+canzeit4)-((((globalT33+canzeit4)/60).floor)*60)).floor)++ "s"++ "\n" ++
							"[Fin]: " ++ ((globalT4/60).floor) ++ "'" ++ ((globalT4-(((globalT4/60).floor)*60)).floor)++ "s";
							fulltexttime.postln;

							texttime = "Duration of this Soliloque: " ++ ((globalT4/60).floor) ++ " min.  " ++ ((globalT4-(((globalT4/60).floor)*60)).floor)++ " sec.";

							durationText = StaticText.new( w, Rect.new(490 , 470 , 400, 42));
							durationText.string = texttime;

							//---------------- Teil IV.1 --------------

							// -------------------------------------------------
							//------------- cantus firmus doux partie IV -------
							// -------------------------------------------------
/* ancienne version avec envgen
t4c1p1 = Pbind(\instrument, \instKlankd, \specf, [fmin1, fmin2, fmin3, fmin4], \bufNum, soundFile5.bufnum, \start, tmin5 + (0.08*sinFunc), \speed, 1.2, \len, 0.1, \dur, Pseq([puls1], (t4dur/(puls1))),\pan, sinFunc - (Pfunc({EnvGen.ar(envelopes.at(1), levelScale: 0.6, levelBias: 0.35)})), \vol, 0.5, \envIdx, 1);
t4c1p2 = Pbind(\instrument, \instKlankd, \specf, [fmin1, fmin2, fmin3, fmin4], \bufNum, soundFile5.bufnum, \start, tmin5-(0.07*cosFunc), \speed, Pfunc({EnvGen.ar(envelopes.at(1), levelScale: rrand(1.2, 1.6, 1.4, 1.8), levelBias: 0.35)}),
\len, (0.3 + (0.5*squared(compFunc1))), \dur, Pseq([puls1], (t4dur/(puls1))),\pan, (-1*cosFunc) + (Pfunc({EnvGen.ar(envelopes.at(1), levelScale: 0.5, levelBias: 0.35)})), \vol, 0.005, \envIdx, 3);

t4c2p1 = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile2.bufnum, \start, tmin2 + (0.08*cosFunc), \speed, 0.8, \len, 0.15, \dur,  Pseq([puls2], (t4dur/(puls2))),\pan, cosFunc + (Pfunc({EnvGen.ar(envelopes.at(3), levelScale: 0.7, levelBias: 0.35)})), \vol, 0.5, \envIdx, 1);
t4c2p2 = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile2.bufnum, \start, tmin2 + (0.07*sinFunc), \speed, 0.8, \len, 0.18, \dur,  Pseq([puls2], (t4dur/(puls2))),\pan, (-0.1*sinFunc) - (Pfunc({EnvGen.ar(envelopes.at(3), levelScale: 0.5, levelBias: 0.35)})), \vol, 0.05, \envIdx, 1);
t4c2p3 = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4],  \spect, [0.9, 1.3, 1.5, 2], \bufNum, soundFile2.bufnum, \start, tmin2- (0.05*cosFunc), \speed, 2- Pfunc({EnvGen.ar(envelopes.at(5), levelScale: rrand(1.1, 0.5, 0.8), levelBias: 0.25)}), \len, ((0.35*sqrt(fmin2/fmin5))+ (0.5*squared(compFunc1))), \dur,  Pseq([puls3], (t4dur/(puls3))),
\pan, -1*sinFunc + (Pfunc({EnvGen.ar(envelopes.at(2), levelScale: 0.7, levelBias: 0.35)})), \vol, 0.005*(Pfunc({EnvGen.ar(envelopes.at(2), levelScale: 1, levelBias: 0.35)})), \envIdx, 5); */

// Nouvelle version malheureusement manque les petits glissandi qui rendaient la chose interessante

// avec son 5
t4c1p1 = Pbind(\instrument, \instKlankd, \specf, [fmin1, fmin2, fmin3, fmin4], \bufNum, soundFile5.bufnum, \start, tmin5 + (0.09*sinFunc), \speed, 1.2 - (0.1*cosFunc), \len, 0.1, \dur, Pseq([puls1], (t4dur/(puls1))),\pan, sinFunc*(cosFunc-0.5) , \vol, 0.2 + (0.4*sinFunc*cosFunc), \envIdx, 1);
t4c1p2 = Pbind(\instrument, \instKlankd, \specf, [fmin1, fmin2, fmin3, fmin4], \bufNum, soundFile5.bufnum, \start, tmin5-(0.08*cosFunc), \speed, 1.1 + (0.09*sinFunc),\len, (0.3 + (0.5*squared(compFunc1))), \dur, Pseq([puls1], (t4dur/(puls1))),\pan, (-1*cosFunc)*(0.3+sinFunc), \vol, 0.2, \envIdx, 3);

// avec son 2
t4c2p1 = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile2.bufnum, \start, tmin2 + (0.08*cosFunc), \speed, 0.8, \len, 0.15, \dur,  Pseq([puls2], (t4dur/(puls2))),\pan, cosFunc*(sinFunc-cosFunc), \vol, 0.2- (0.1*sinFunc), \envIdx, 1);
t4c2p2 = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile2.bufnum, \start, tmin2 + (0.08*sinFunc), \speed, 0.9 + (0.1*sinFunc*cosFunc), \len, 0.18, \dur,  Pseq([puls2], (t4dur/(puls2))),\pan, ((-0.1*sinFunc)*(0.2-sinFunc)), \vol, 0.2, \envIdx, 1);
t4c2p3 = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4],  \spect, [0.9, 1.3, 1.5, 2], \bufNum, soundFile2.bufnum, \start, tmin2- (0.06*cosFunc), \speed, 0.7, \len, ((0.35*sqrt(fmin2/fmin5))+ (0.5*squared(compFunc1))), \dur,  Pseq([puls3], (t4dur/(puls3))),\pan, (-1*sinFunc)*( 0.5 + sinFunc*cosFunc), \vol, 0.2 + (0.3*cosFunc), \envIdx, 5);

// avec son 4
t4c3p1 = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile4.bufnum, \start, tmin4 - (0.06*cosFunc), \speed, (l1/l3) + (0.1*sinFunc), \len, (0.18 + (0.2*squared(sinFunc))), \dur,  Pseq([puls4], (t4dur/(puls4))),\pan, cosFunc*(compFunc1-sinFunc), \vol, 0.2 + (0.4*compFunc1), \envIdx, 1);
t4c3p2 = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile4.bufnum, \start, tmin4 - (0.05*sinFunc), \speed, (l1/l3) + (0.1*cosFunc), \len, 0.18, \dur,  Pseq([puls5], (t4dur/(puls5))),\pan, ((0.3*cosFunc)*(0.5-sinFunc)), \vol, 0.2, \envIdx, 1);

// fade out du cantus firmus

// avec son 5
t4c1p1d = Pbind(\instrument, \instKlankd, \specf, [fmin1, fmin2, fmin3, fmin4], \bufNum, soundFile5.bufnum, \start, tmin5 + (0.09*sinFunc), \speed, 1.3-(0.1*cosFunc), \len, 0.1, \dur, Pseq([puls1], (t4durd/(puls1))),\pan, sinFunc*(cosFunc-0.5) , \vol, 0.2 - ((puls1/t4durd)*2*rampFunc), \envIdx, 1);
t4c1p2d = Pbind(\instrument, \instKlankd, \specf, [fmin1, fmin2, fmin3, fmin4], \bufNum, soundFile5.bufnum, \start, tmin5-(0.08*cosFunc), \speed, 1.0+ (0.09*sinFunc),\len, (0.3 + (0.5*squared(compFunc1))), \dur, Pseq([puls1], (t4durd/(puls1))),\pan, (-1*cosFunc)*(0.3+sinFunc), \vol, 0.1 - ((puls1/t4durd)*2*rampFunc), \envIdx, 3);

// avec son 2
t4c2p1d = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile2.bufnum, \start, tmin2 + (0.08*cosFunc), \speed, 0.9, \len, 0.15, \dur,  Pseq([puls2], (t4durd/(puls2))),\pan, cosFunc*(sinFunc-cosFunc), \vol, 0.2- ((puls2/t4durd)*2*rampFunc), \envIdx, 1);
t4c2p2d = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile2.bufnum, \start, tmin2 + (0.08*sinFunc), \speed, 0.8 + (0.1*sinFunc*cosFunc), \len, 0.18, \dur,  Pseq([puls2], (t4durd/(puls2))),\pan, ((-0.1*sinFunc)*(0.2-sinFunc)), \vol, 0.1 - ((puls2/t4durd)*2*rampFunc), \envIdx, 1);
t4c2p3d = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4],  \spect, [0.9, 1.3, 1.5, 2], \bufNum, soundFile2.bufnum, \start, tmin2- (0.06*cosFunc), \speed, 0.8, \len, ((0.35*sqrt(fmin2/fmin5))+ (0.5*squared(compFunc1))), \dur,  Pseq([puls3], (t4durd/(puls3))),\pan, (-1*sinFunc)*( 0.5 + sinFunc*cosFunc), \vol, 0.2- ((puls3/t4durd)*2*rampFunc), \envIdx, 5);

// avec son 4
t4c3p1d = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile4.bufnum, \start, tmin4 - (0.06*cosFunc), \speed, (l1/l3) + (0.05*sinFunc), \len, (0.18 + (0.2*squared(sinFunc))), \dur,  Pseq([puls4], (t4durd/(puls4))),\pan, cosFunc*(compFunc1-sinFunc), \vol, 0.2 - ((puls4/t4durd)*2*rampFunc), \envIdx, 1);
t4c3p2d = Pbind(\instrument, \instKlankd, \specf, [fmin6, fmin5, fmin3, fmin4], \bufNum, soundFile4.bufnum, \start, tmin4 - (0.05*sinFunc), \speed, (l1/l3) + (0.15*cosFunc), \len, 0.18, \dur,  Pseq([puls5], (t4durd/(puls5))),\pan, ((0.3*cosFunc)*(0.5-sinFunc)), \vol, 0.2 - ((puls5/t4durd)*2*rampFunc), \envIdx, 1);


// -------------------------------------------------
//------------- Canon 1 (a partir son 5) --------------
// -------------------------------------------------

canon1= {
	var  t1, t2, t3,
	cella, cellb, cellc, celld, celle, cellf, cellg, cellh, celli,
	czeita, czeitb, czeitc, czeitd, czeite, czeitf, czeitg, czeith, czeiti;

	//-----Variables
	t1 = (tmin5 + tmx5)/2; t2 = (tmax5 + tmx5)/2; t3 = (tmax5 + tmin5)/2; // positions de lecture
	czeita = 0;
	czeitb = czeita + 4*puls;
	czeitc = czeitb + (3*puls);
	czeitd = czeitc + (2*puls);
	czeite = czeitd + ((5/2)*puls);
	czeitf = czeite + ((l2/3)*puls);
	czeitg = czeitf + (puls);
	czeith = czeitg + (puls);
	czeiti = czeith + ((3/2)*puls);   // czeita + (15+(l2/3))*puls
	// Duree canon 1: (16+(l2/3))*puls

	//-----Cella : montee descente
	cella = {
		var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;

		lha = min((max(lh5, 2)),5); coeff = min ((fmx1/fmx5), (fmx5/fmx1));
		dura1 = puls*(lha/10); dura2 = puls*((10-lha)/10);  //duree locales entre debut et fin de cellule

		cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile5.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.5*coeff/dura1)*55*rampFunc)), \len, (0.15+((0.5*coeff/dura1)*3.5*rampFunc))*coeff,\dur, Pseq([0.5*coeff], dura1/(0.5*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
		cap2 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[40, 550, 2300, 1800], inf), \filtQ, 0.6, \bufNum, soundFile5.bufnum, \start, t2 + (-0.10*sinFunc)-(0.15*cosFunc), \speed, (0+((0.33*coeff/dura1)*20*rampFunc)), \len, (0.15+((0.33*coeff/dura1)*1.8*rampFunc))*coeff, \dur, Pseq([0.33*coeff], dura1/(0.33*coeff)), \pan, (-0.9*sinFunc)+(0.8*cosFunc), \vol, ((-0.7*sinFunc)+(0.9*cosFunc)), \envIdx, 1);
		cap3 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[1400,  1500, 600, 300, 400, 1600], inf), \filtQ, 0.8,\bufNum, soundFile5.bufnum, \start, t1 + (0.15*sinFunc)+(0.13*cosFunc), \speed,  (0.5+((0.25*coeff/dura1)*15*rampFunc)), \len, (0.15+((0.25*coeff/dura1)*1*rampFunc))*coeff, \dur, Pseq([0.25*coeff], dura1/(0.25*coeff)), \pan, (-0.8*sinFunc)-(0.9*cosFunc), \vol, ((0.7*sinFunc)-(0.5*cosFunc)), \envIdx, 1);
		cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile5.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.4*coeff/dura2)*5*rampFunc)), \len, (0.35-((0.4*coeff/dura1)*2*rampFunc))*coeff, \dur, Pseq([0.4*coeff], dura2/(0.4*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.4*coeff/dura2)*12*rampFunc))), \envIdx, 1);
		cap5 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[40, 550, 2300, 1800], inf), \filtQ, 0.6, \bufNum, soundFile5.bufnum, \start, t2 + (-0.10*sinFunc)-(0.15*cosFunc), \speed, (2-((0.3*coeff/dura2)*2*rampFunc)), \len, (0.25-((0.3*coeff/dura1)*1*rampFunc))*coeff, \dur, Pseq([0.3*coeff], dura2/(0.3*coeff)), \pan, (-0.9*sinFunc)+(0.8*cosFunc), \vol, (((-0.7*sinFunc)+(0.9*cosFunc))*(1.1-((0.3*coeff/dura2)*12*rampFunc))), \envIdx, 1);
		cap6 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[1400,  1500, 600, 300, 400, 1600], inf), \filtQ, 0.8,\bufNum, soundFile5.bufnum, \start, t1 + (0.15*sinFunc)+(0.13*cosFunc), \speed,  (2-((0.2*coeff/dura2)*10*rampFunc)), \len, 0.15*coeff, \dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (-0.8*sinFunc)-(0.9*cosFunc), \vol, (((0.7*sinFunc)-(0.5*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

		Ptpar([0.00, cap1, 0.00, cap2, 0.00, cap3 ,
			dura1, cap4, dura1, cap5, dura1, cap6], 1);
	};
	//-----Cellb : constante mouillee montee
	cellb = {
		var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
		lhb = lh4; coeff = min ((fmin1/fmin5), (fmin5/fmin1));

		durb1 = puls*(lhb/10); durb2 = puls*((10-lhb)/10);  //duree locales entre debut et fin de cellule

		cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.20*coeff,
			\dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
		cbp2 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[1000, 200, 400, 500], inf), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t2 + (-0.10*sinFunc)+(0.08*cosFunc), \speed, 1, \len, 0.15*coeff,
			\dur, Pseq([0.15*coeff], durb1/(0.15*coeff)), \pan, (-1+((14*rampFunc*0.15*coeff)/durb1)), \vol, (0.5*sinFunc)-(0.7*cosFunc), \envIdx, 1);
		cbp3 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[250, 500, 100, 200], inf), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t3 + (0.10*sinFunc)-(0.10*cosFunc), \speed, 0.5, \len, 0.10*coeff,
			\dur, Pseq([0.10*coeff], durb1/(0.10*coeff)), \pan, (-1+((14*rampFunc*0.10*coeff)/durb1)), \vol, (0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
		cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff,
			\dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);
		cbp5 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[1000, 200, 400, 500], inf), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t2 + (-0.10*sinFunc)+(0.08*cosFunc), \speed, 1+((15*rampFunc*0.15*coeff)/durb2), \len, 0.15*coeff,
			\dur, Pseq([0.15*coeff], durb2/(0.15*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.15*coeff)/durb2))), \vol, ((0.5*sinFunc)-(0.7*cosFunc))/(1+((100*rampFunc*0.15*coeff)/durb2)), \envIdx, 1);
		cbp6 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[250, 500, 100, 200], inf), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t3 + (0.10*sinFunc)-(0.10*cosFunc), \speed, 0.5+((15*rampFunc*0.10*coeff)/durb2), \len, 0.10*coeff,
			\dur, Pseq([0.10*coeff], durb2/(0.10*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.10*coeff)/durb2))), \vol, ((0.7*sinFunc)-(0.5*cosFunc))/(1+((100*rampFunc*0.10*coeff)/durb2)), \envIdx, 1);

		Ptpar([0.00, cbp1, 0.00, cbp2, 0.00, cbp3 ,
			durb1, cbp4, durb1, cbp5, durb1, cbp6    ], 1);
	};
	//-----Cellc : constante transposee
	cellc = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

		lhx = 0.1*(max(lh3, (1-lh3))); coeff = max((min ((fmax1/fmax5), (fmax5/fmax1))), 0.5);
		durx1 = puls*lhx; durx2 = puls*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp2 = Pbind(\instrument, \instRLPF, \filtFrq, 1500 +(1000*squared(0.5*cosFunc))+(650*cosFunc), \filtQ, 0.6, \bufNum, soundFile5.bufnum, \start, t1 + (-0.08*sinFunc)+(0.14*cosFunc), \speed, 3.5+(-0.01*sinFunc)-(0.01*cosFunc), \len, 0.15*coeff, \dur, Pseq([0.15*coeff], durx1/(0.15*coeff)), \pan, (0.8-((6*rampFunc*0.15*coeff)/durx1)-compFunc1), \vol, 0.6+(0.3*sinFunc)-(0.2*cosFunc), \envIdx, 1);
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile5.bufnum, \start, t1 + (0.12*sinFunc)+(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.10*coeff, \dur, Pseq([0.10*coeff], durx1/(0.10*coeff)), \pan, (0.8-(0.5*compFunc1)), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp5 = Pbind(\instrument, \instRLPF, \filtFrq, 1500 +(1000*squared(0.5*cosFunc))+(650*cosFunc), \filtQ, 0.6, \bufNum, soundFile5.bufnum, \start, t2 + (-0.08*sinFunc)+(0.14*cosFunc), \speed, (3.5*(fmin1/fmin5))+(-0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.15*coeff, \dur, Pseq([0.15*coeff], durx2/(0.15*coeff)), \pan, Pseq(#[0, -1, 1], inf), \vol, 0.6+(0.3*sinFunc)-(0.2*cosFunc), \envIdx, 1);
		cxp6 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile5.bufnum, \start, t2 + (0.12*sinFunc)+(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.10*coeff, \dur, Pseq([0.10*coeff], durx2/(0.10*coeff)), \pan, Pseq(#[-1, 0, 1], inf), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 1);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};
	//-----Celld : craquements plus ou moins resonants
	celld = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

		lhx = (lh2/10); coeff = min((l1/l5), (l5/l1));
		durx1 = (czeite-czeitd); //ATTENTION DUREE DANS CANON 1 : jusqu'a f !

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

		Ptpar([0.00, cxp1 ], 1);
	};

	//-----Celle : montee discrete
	celle = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

		lhx = max((lh1/10), ((10-lh1)/10)); coeff = min ((fmx2/fmx5), (fmx5/fmx2));
		durx1 = puls*lhx; durx2 = puls*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, t3+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
		cxp2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, t2-0.8*cosFunc, \speed, 1.2+((20*rampFunc*0.195*coeff)/durx1), \len, 0.190*coeff,\freq, fmin5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.195*coeff], durx1/(0.195*coeff)), \pan,(-1)*(cos((0.3)*exp(rampFunc))), \vol,  0.7+((3*rampFunc*0.195*coeff)/durx1), \envIdx, 5);
		cxp3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, t1-0.7*sinFunc, \speed, 1.8+((13*rampFunc*0.190*coeff)/durx1), \len, 0.185*coeff,\freq, fmx5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx1/(0.190*coeff)), \pan, (-1)*(cos(exp((0.3)*rampFunc))), \vol,  0.7+((3*rampFunc*0.190*coeff)/durx1), \envIdx, 5);
		cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, t3+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((7*rampFunc*0.2*coeff)/durx2), \envIdx, 5);
		cxp5 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, t2-0.8*cosFunc, \speed, 3.2, \len, 0.190*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmin5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.195*coeff], durx2/(0.195*coeff)), \pan, (-0.5*sinFunc)-(0.4*cosFunc), \vol,  1.1-((10*rampFunc*0.195*coeff)/durx2), \envIdx, 5);
		cxp6 = Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, t1-0.7*sinFunc, \speed, 3.1, \len, 0.185*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmx5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx2/(0.190*coeff)), \pan, (0.7*sinFunc)+(0.8*cosFunc), \vol,  1.1-((10*rampFunc*0.190*coeff)/durx2), \envIdx, 5);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};

	//-----Cellf : modulation en anneau constante, puis de craquements a ringmod
	cellf = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

		lhx = (lh6/10); coeff = min((max ((l2/l5), (l5/l2))), 1.5); coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
		durx1 = puls*lhx; durx2 = puls*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t1+0.07*cosFunc, \speed, 0.8, \len, 0.075*coeff, \dur, Pseq([0.08*coeff], durx1/(0.08*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 0.8-((10*rampFunc*0.08*coeff)/durx1), \envIdx, 3);
		cxp2 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t2+0.05*sinFunc, \speed, 1.2, \len, 0.065*coeff, \dur, Pseq([0.07*coeff], durx1/(0.07*coeff)), \pan, 0.7*(cosFunc-sinFunc), \vol, 0.8-((10*rampFunc*0.07*coeff)/durx1), \envIdx, 3);
		cxp3 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t3-0.06*cosFunc, \speed, 0.9, \len, 0.055*coeff, \dur, Pseq([0.06*coeff], durx1/(0.06*coeff)), \pan, -0.7*(cosFunc-sinFunc), \vol, 0.8-((10*rampFunc*0.07*coeff)/durx1), \envIdx, 3);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t1+0.07*cosFunc, \speed, 0.4, \len, 0.075*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.08*coeff2], durx2/(0.08*coeff2)), \pan, ((10*rampFunc*0.08*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 0.7, \envIdx, 3);
		cxp5 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t2+0.05*sinFunc, \speed, 1, \len, 0.065*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.07*coeff2], durx2/(0.07*coeff2)), \pan, ((10*rampFunc*0.07*coeff2)/durx2)*(0.7*(cosFunc-sinFunc)), \vol, 0.7, \envIdx, 3);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t3-0.06*cosFunc, \speed, 0.7, \len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.06*coeff2], durx2/(0.06*coeff2)), \pan, ((10*rampFunc*0.06*coeff2)/durx2)*(-0.7*(cosFunc-sinFunc)), \vol, 0.7, \envIdx, 3);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};

	//-----Cellg
	cellg = {
		var cxp1, cxp3, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
		coeff = (l3/l5); durx1 = puls;   //duree locales entre debut et fin de cellule
		lx = l5;
		r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
		rm = (r1 + r2 + r3 + r4 + r5)/5; // attention pour avoir une duree de puls  ne pas diviser par rm

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf), \len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.1+(-0.56*sinFunc)+(0.51*cosFunc), \envIdx, 3);
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile5.bufnum, \start, t2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),\len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.1+(0.5*sinFunc)+(0.54*cosFunc), \envIdx, 3);

		Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
	};

	//-----Cellh : craquements constants puis vers ringmod
	cellh = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = (lh6/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
		durx1 = puls*lhx; durx2 = puls*(2-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp2 = Pbind(\instrument, \instRLPF, \filtFrq, 1900 +(600*squared(0.6*cosFunc))+(400*sinFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, t1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 3.5+(-0.02*sinFunc)+(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.025*coeff], durx1/(0.027*coeff)), \pan, (-0.8+((15*rampFunc*0.027*coeff)/durx1)), \vol, 0.5+(0.2*sinFunc)+(-0.1*cosFunc), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 0.8+(0.3*sinFunc)+(-0.4*cosFunc), \envIdx, 1);
		cxp5 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 3.5+(-0.02*sinFunc)+(0.01*cosFunc), \len, 0.065*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff2)/durx2))), \dur, Pseq([0.025*coeff2], durx2/(0.025*coeff2)), \pan, (1-((10*rampFunc*0.027*coeff2)/durx2))*(0.7*sinFunc), \vol, 0.8-(0.26*sinFunc)+(0.34*cosFunc), \envIdx, 1);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3,
			durx1, cxp4, durx1, cxp5, durx1, cxp6], 1);
	};

	//-----Celli : constante transposee
	celli = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

		lhx = min((lh4/10), ((10-lh4)/10)); coeff = min((max ((l3/l5), (l5/l3))), 1.5); coeff2 = max((min ((l3/l5), (l5/l3))), 0.5);
		durx1 = puls*lhx; durx2 = puls*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff)/durx1))), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, ((18*rampFunc*0.03*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 0.7+(-0.9*cosFunc*sinFunc)+(0.8*cosFunc), \envIdx, 1);
		cxp2 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 2.5+(-0.02*sinFunc)+(0.01*cosFunc), \len, 0.065*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff)/durx1))), \dur, Pseq([0.025*coeff], durx1/(0.025*coeff)), \pan, ((18*rampFunc*0.027*coeff)/durx1)-0.8+(-0.2*sinFunc), \vol, 0.8+(0.8*sinFunc)+(0.73*sinFunc*cosFunc), \envIdx, 1);
		cxp3 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 2.5+(0.01*sinFunc)+(0.01*cosFunc), \len, 0.055*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff)/durx1))), \dur, Pseq([0.027*coeff], durx1/(0.027*coeff)), \pan, ((18*rampFunc*0.025*coeff)/durx1)-0.8+(-0.1*cosFunc), \vol, 0.8+(-0.7*sinFunc)+(0.8*cosFunc), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (2.5-((20*rampFunc*0.035*coeff2)/durx2)+(0.01*sinFunc)-(0.01*cosFunc)), \len, 0.075*(coeff2+((coeff-coeff2)*((10*rampFunc*0.035*coeff2)/durx2))), \dur, Pseq([0.035*coeff2], durx2/(0.035*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.035*coeff2)/durx2)), \envIdx, 1);
		cxp5 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, (2.5-((25*rampFunc*0.03*coeff2)/durx2)+(-0.02*sinFunc)+(0.01*cosFunc)),\len, 0.065*(coeff2+((coeff-coeff2)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.03*coeff2)/durx2)), \envIdx, 1);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, t3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, (2.5-((27*rampFunc*0.032*coeff2)/durx2)+(0.01*sinFunc)+(0.01*cosFunc)), \len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.032*coeff2)/durx2))), \dur, Pseq([0.032*coeff2], durx2/(0.032*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.032*coeff2)/durx2)), \envIdx, 1);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};


	//-----Partitur du canon 1
	// Ptpar([0.00, cellg.value], 1);

	Ptpar([czeita, cella.value,
		czeitb, cellb.value,
		czeitc, cellc.value,
		czeitd, celld.value,
		czeite, celle.value,
		czeitf, cellf.value,
		czeitg, cellg.value,
		czeith, cellh.value,
		czeiti, celli.value
		], 1);
};

// -------------------------------------------------
//------------- Canon 2 (a partir son 2) --------------
// -------------------------------------------------
canon2= {
	var  t1, t2, t3,
	cellc, celld, celle, cellf, cellg, cellh, celli, // voix son 2
	czeitc, czeitd, czeite, czeitf, czeitg, czeith, czeiti,
	celld2, celle2, czeitd2, czeite2; // insertion voix son 6

	//-----Variables
	t1 = (tmin2 + tmx2)/2; t2 = (tmax2 + tmx2)/2; t3 = (tmax2 + tmin2)/2; // positions de lecture
	czeitc = 0;
	czeitd = czeitc + (2*pulsd);
	czeite = czeitd + (pulsd);
	czeitf = czeite + ((l2/3)*pulsd);
	czeitg = czeitf + (pulsd); // dure plus longtemps
	czeith = czeitg + ((3/2)*pulsd);
	czeiti = czeith + (2*pulsd);  // czeitc + ((15/2)+ (l2/3))*pulsd

	czeitd2 = czeitg + pulsd;
	czeite2 = czeitd2 + ((3/2)*pulsd); // czeitc + ((13/2)+ (l2/3))*pulsd e dure 2 fois plus longtemps ici

	// Duree canon 2 : ((17/2)+ (l2/3))*pulsd


	// VOIX 1 (son 2)
	//-----Cellc : constante transposee
	cellc = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

		lhx = 0.1*(max(lh3, (1-lh3))); coeff = max ((min ((fmax1/fmax2), (fmax2/fmax1))), 0.5);
		durx1 = (czeite-czeitc)*lhx; durx2 = (czeite-czeitc)*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, t1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (-0.8+((10*rampFunc*0.20*coeff)/durx1)), \vol, ((0.4*10*rampFunc*0.20*coeff)/durx1), \envIdx, 1);
		cxp2 = Pbind(\instrument, \instRLPF, \filtFrq, 1500 +(1000*squared(0.5*cosFunc))+(650*cosFunc), \filtQ, 0.6, \bufNum, soundFile2.bufnum, \start, t1 + (-0.08*sinFunc)+(0.14*cosFunc), \speed, 3.5+(-0.01*sinFunc)-(0.01*cosFunc), \len, 0.15*coeff, \dur, Pseq([0.15*coeff], durx1/(0.15*coeff)), \pan, (-0.8+((10*rampFunc*0.15*coeff)/durx1)), \vol, ((0.4*10*rampFunc*0.15*coeff)/durx1), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin2))+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, (0.2-((10*rampFunc*0.20*coeff)/durx2)), \vol, 0.5-((0.2*10*rampFunc*0.20*coeff)/durx2), \envIdx, 1);
		cxp5 = Pbind(\instrument, \instRLPF, \filtFrq, 1500 +(1000*squared(0.5*cosFunc))+(650*cosFunc), \filtQ, 0.6, \bufNum, soundFile2.bufnum, \start, t2 + (-0.08*sinFunc)+(0.14*cosFunc), \speed, (3.5*(fmin1/fmin2))+(-0.01*sinFunc)-(0.01*cosFunc), \len, 0.15*coeff, \dur, Pseq([0.15*coeff], durx2/(0.15*coeff)), \pan, (0.2-((10*rampFunc*0.15*coeff)/durx2)), \vol, 0.5-((0.2*10*rampFunc*0.15*coeff)/durx2), \envIdx, 1);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};
	//-----Celld : craquements plus ou moins resonants
	celld = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
		lhx = (lh2/10); coeff = min((l1/l2), (l2/l1));
		durx1 = (czeite -czeitd);

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.6-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.8+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		// Zitat
		cxp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, tmx6, \speed, -0.8,\len, durx1,\dur, Pseq([durx1], 1),\pan, 0.7,\vol, ((l2/l3)/10)+ (-0.08*sinFunc)+(0.14*cosFunc),\envIdx, 3); //citation

		Ptpar([0.00, cxp1, 0.00, cxp2], 1);
	};

	//-----Celle : montee discrete
	celle = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
		lhx = max((lh1/10), ((10-lh1)/10)); coeff = min ((fmx3/fmx2), (fmx2/fmx3));
		durx1 = pulsd*lhx; durx2 = pulsd*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, t3+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
		cxp2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, t2-0.8*cosFunc, \speed, 1.2+((20*rampFunc*0.195*coeff)/durx1), \len, 0.190*coeff,\freq, fmin2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.195*coeff], durx1/(0.195*coeff)), \pan,(-1)*(cos((0.3)*exp(rampFunc))), \vol,  0.7+((3*rampFunc*0.195*coeff)/durx1), \envIdx, 5);
		cxp3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, t1-0.7*sinFunc, \speed, 1.8+((13*rampFunc*0.190*coeff)/durx1), \len, 0.185*coeff,\freq, fmx2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx1/(0.190*coeff)), \pan, (-1)*(cos(exp((0.3)*rampFunc))), \vol,  0.7+((3*rampFunc*0.190*coeff)/durx1), \envIdx, 5);
		cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, t3+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);
		cxp5 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, t2-0.8*cosFunc, \speed, 3.2, \len, 0.190*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmin2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.195*coeff], durx2/(0.195*coeff)), \pan, (-0.5*sinFunc)-(0.4*cosFunc), \vol,  1.1-((9*rampFunc*0.195*coeff)/durx2), \envIdx, 5);
		cxp6 = Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, t1-0.7*sinFunc, \speed, 3.1, \len, 0.185*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmx2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx2/(0.190*coeff)), \pan, (0.7*sinFunc)+(0.8*cosFunc), \vol,  1.1-((9*rampFunc*0.190*coeff)/durx2), \envIdx, 5);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};

	//-----Cellf : modulation en anneau constante, puis de craquements a ringmod ICI inverse
	cellf = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = (lh6/10); coeff = min((max ((l3/l2), (l2/l3))), 1.5); coeff2 = max((min ((l2/l3), (l3/l2))), 0.5);
		durx1 = pulsd*lhx; durx2 = pulsd*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t1+0.07*cosFunc, \speed, 0.8, \len, 0.075*coeff, \dur, Pseq([0.08*coeff], durx1/(0.08*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.08*coeff)/durx1), \envIdx, 3);
		cxp2 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t2+0.05*sinFunc, \speed, 1.2, \len, 0.065*coeff, \dur, Pseq([0.07*coeff], durx1/(0.07*coeff)), \pan, 0.7*(cosFunc-sinFunc), \vol, 1-((10*rampFunc*0.07*coeff)/durx1), \envIdx, 3);
		cxp3 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t3-0.06*cosFunc, \speed, 0.9, \len, 0.055*coeff, \dur, Pseq([0.06*coeff], durx1/(0.06*coeff)), \pan, -0.7*(cosFunc-sinFunc), \vol, 1-((10*rampFunc*0.07*coeff)/durx1), \envIdx, 3);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t1+0.07*cosFunc, \speed, 0.4, \len, 0.075*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.08*coeff2], durx2/(0.08*coeff2)), \pan, ((10*rampFunc*0.08*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);
		cxp5 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t2+0.05*sinFunc, \speed, 1, \len, 0.065*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.07*coeff2], durx2/(0.07*coeff2)), \pan, ((10*rampFunc*0.07*coeff2)/durx2)*(0.7*(cosFunc-sinFunc)), \vol, 1, \envIdx, 3);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t3-0.06*cosFunc, \speed, 0.7, \len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.06*coeff2], durx2/(0.06*coeff2)), \pan, ((10*rampFunc*0.06*coeff2)/durx2)*(-0.7*(cosFunc-sinFunc)), \vol, 1, \envIdx, 3);

		Ptpar([0.00, cxp4, 0.00, cxp5, 0.00, cxp6,
			durx1, cxp1, durx1, cxp2, durx1, cxp3  ], 1); //inversement des deux moities
	};

	//-----Cellg
	cellg = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
		coeff = (l3/l2); durx1 = 2*pulsd;   //duree locales entre debut et fin de cellule
		lx = l2;
		r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
		rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsd  ne pas diviser par 5

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[1000, 7000, 100, 140, 1900], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], (durx1/rm)), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.3+((0.5*rampFunc*rm)/durx1)+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 3);
		cxp2 = Pbind(\instrument, \instRLPF, \filtFrq, 1500 +(650*cosFunc), \filtQ, 0.6, \bufNum, soundFile2.bufnum, \start, t2 + (-0.08*sinFunc)+(0.07*cosFunc), \speed, Pseq(#[1, 2, 0.5, 4, 1, 4, 0.5, 2], inf), \len, Pseq([r3, r1, r4, r5, r2], inf), \dur, Pseq([r3, r1, r4, r5, r2], durx1/rm), \pan,  (0.8-((6*rampFunc*rm)/durx1)-compFunc1), \vol, 0.4+((0.5*rampFunc*rm)/durx1)+(0.3*sinFunc)-(0.2*cosFunc), \envIdx, 3);
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 -(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile2.bufnum, \start, t2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf), \len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6-((0.5*rampFunc*rm)/durx1)+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 3);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ], 1);
	};

	//-----Cellh : craquements constants puis vers ringmod
	cellh = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = (lh6/10); coeff = max((min ((l3/(2*l2)), (l2/(2*l3)))), 0.5); coeff2 = min((max (((2*l2)/l3), (((2*l3)/l2)))), 1.5);
		durx1 = pulsd*lhx; durx2 = pulsd*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp2 = Pbind(\instrument, \instRLPF, \filtFrq, 1900 +(600*squared(0.6*cosFunc))+(400*sinFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, t1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 3.5+(-0.02*sinFunc)+(0.01*cosFunc),
			\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.025*coeff], durx1/(0.027*coeff)), \pan, (-0.8+((15*rampFunc*0.027*coeff)/durx1)), \vol, 0.5+(0.2*sinFunc)+(-0.1*cosFunc), \envIdx, 1);
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1700 +(1000*squared(0.5*sinFunc))-(700*sinFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, t3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 3.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, (1.01-(cos((-1)*compFunc1)))*(coeff/3), \dur, Pseq([0.027*coeff], durx1/(0.025*coeff)), \pan, (0.3-((4*rampFunc*0.025*coeff)/durx1)), \vol, 0.6+(0.5*sinFunc)+(0.2*cosFunc), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);
		cxp5 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 3.5+(-0.02*sinFunc)+(0.01*cosFunc),
			\len, 0.065*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff2)/durx2))), \dur, Pseq([0.025*coeff2], durx2/(0.025*coeff2)), \pan, (1-((10*rampFunc*0.027*coeff2)/durx2))*(0.7*sinFunc), \vol, 1, \envIdx, 1);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 3.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.055*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff2)/durx2))), \dur, Pseq([0.027*coeff2], durx2/(0.027*coeff2)), \pan, (1-((10*rampFunc*0.025*coeff2)/durx2))*(-0.1*sinFunc), \vol, 1, \envIdx, 1);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3,
			durx1, cxp4, durx1, cxp5, durx1, cxp6], 1);
	};

	//-----Celli : constante transposee
	celli = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

		lhx = min((lh4/10), ((10-lh4)/10)); coeff = min((max ((l3/l2), (l2/l3))), 1.5); coeff2 = max((min ((l3/l2), (l2/l3))), 0.5);
		durx1 = pulsd*lhx; durx2 = pulsd*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff)/durx1))), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, ((18*rampFunc*0.03*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, ((10*rampFunc*0.03*coeff)/durx1), \envIdx, 1);
		cxp2 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 2.5+(-0.02*sinFunc)+(0.01*cosFunc),\len, 0.065*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff)/durx1))), \dur, Pseq([0.025*coeff], durx1/(0.025*coeff)), \pan, ((18*rampFunc*0.027*coeff)/durx1)-0.8+(-0.2*sinFunc), \vol, ((10*rampFunc*0.027*coeff)/durx1), \envIdx, 1);
		cxp3 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 2.5+(0.01*sinFunc)+(0.01*cosFunc),\len, 0.055*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff)/durx1))), \dur, Pseq([0.027*coeff], durx1/(0.027*coeff)), \pan, ((18*rampFunc*0.027*coeff)/durx1)-0.8+(-0.1*cosFunc), \vol, ((10*rampFunc*0.027*coeff)/durx1), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (2.5-((20*rampFunc*0.035*coeff2)/durx2)+(0.01*sinFunc)-(0.01*cosFunc)),\len, 0.075*(coeff2+((coeff-coeff2)*((10*rampFunc*0.035*coeff2)/durx2))), \dur, Pseq([0.035*coeff2], durx2/(0.035*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.035*coeff2)/durx2)), \envIdx, 1);
		cxp5 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, (2.5-((25*rampFunc*0.03*coeff2)/durx2)+(-0.02*sinFunc)+(0.01*cosFunc)), \len, 0.065*(coeff2+((coeff-coeff2)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.03*coeff2)/durx2)), \envIdx, 1);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, t3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, (2.5-((27*rampFunc*0.032*coeff2)/durx2)+(0.01*sinFunc)+(0.01*cosFunc)), \len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.032*coeff2)/durx2))), \dur, Pseq([0.032*coeff2], durx2/(0.032*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.032*coeff2)/durx2)), \envIdx, 1);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};

	// VOIX 2 (son 6)
	//-----Celld : craquements plus ou moins resonants
	celld2 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
		lhx = (lh2/10); coeff = min((l1/l6), (l6/l1));
		durx1 = 2*pulsd;

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (-0.3+((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(0.2*sinFunc)+(-0.1*cosFunc), \envIdx, 1);
		// Zitat
		cxp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, 0, \speed, 1.2,\len, durx1,\dur, Pseq([durx1], 1),\pan, -0.4,\vol, ((l6/l5)/8)+(-0.08*sinFunc)+(0.14*cosFunc),\envIdx, 3); //citation


		Ptpar([0.00, cxp1, 0.00, cxp2], 1);
	};

	//-----Celle : montee discrete
	celle2 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
		lhx = max((lh1/10), ((10-lh1)/10)); coeff = min ((fmx3/fmx6), (fmx6/fmx3));
		durx1 = 2*pulsd*lhx; durx2 = 2*pulsd*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t3+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax6, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
		cxp2 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t2-0.8*cosFunc, \speed, 1.2+((20*rampFunc*0.195*coeff)/durx1), \len, 0.190*coeff,\freq, fmin6, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.195*coeff], durx1/(0.195*coeff)), \pan,(-1)*(cos((0.3)*exp(rampFunc))), \vol,  0.7+((3*rampFunc*0.195*coeff)/durx1), \envIdx, 5);
		cxp3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t1-0.7*sinFunc, \speed, 1.8+((13*rampFunc*0.190*coeff)/durx1), \len, 0.185*coeff,\freq, fmx6, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx1/(0.190*coeff)), \pan, (-1)*(cos(exp((0.3)*rampFunc))), \vol,  0.7+((3*rampFunc*0.190*coeff)/durx1), \envIdx, 5);
		cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t3+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax6, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);
		cxp5 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t2-0.8*cosFunc, \speed, 3.2, \len, 0.190*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmin6, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.195*coeff], durx2/(0.195*coeff)), \pan, (-0.5*sinFunc)-(0.4*cosFunc), \vol,  1.1-((9*rampFunc*0.195*coeff)/durx2), \envIdx, 5);
		cxp6 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t1-0.7*sinFunc, \speed, 3.1, \len, 0.185*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmx6, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx2/(0.190*coeff)), \pan, (0.7*sinFunc)+(0.8*cosFunc), \vol,  1.1-((9*rampFunc*0.190*coeff)/durx2), \envIdx, 5);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};

	//-----Partitur du canon 2
	Ptpar([
		czeitc, cellc.value,
		czeitd, celld.value,
		czeite, celle.value,
		czeitf, cellf.value,
		czeitg, cellg.value,
		czeith, cellh.value,
		czeiti, celli.value,
		czeitd2, celld2.value,
		czeite2, celle2.value
		], 1);
};

// -----------------------------------
//------------- Canon 3 --------------
// -----------------------------------

canon3= {
	var  t1, t2, t3, z1, z2, z3,
	cella, cellb, cellc, celld, celle, cellf, cellg, cellh, celli,
	czeita, czeitb, czeitc, czeitd, czeite, czeitf, czeitg, czeith, czeiti,
	cella2, cellb2, cellc2, celld2, celle2, cellf2, cellg2, cellh2, celli2,
	czeita2, czeitb2, czeitc2, czeitd2, czeite2, czeitf2, czeitg2, czeith2, czeiti2,
	zitat1, zitat2, zitat3, zitat4, zitat5, zitat6, zitat7, zitat8, zitat9,
	zeitzi1, zeitzi2, zeitzi3, zeitzi4, zeitzi5, zeitzi6, zeitzi7, zeitzi8, zeitzi9;

	//-----Variables
	t1 = (tmin1 + tmx1)/2; t2 = (tmax1 + tmx1)/2; t3 = (tmax1 + tmin1)/2; // positions de lecture voix1 (son1)
	z1 = (tmin6 + tmx6)/2; z2 = (tmax6 + tmx6)/2; z3 = (tmax6 + tmin6)/2; // positions de lecture voix2 (son6)

	czeita = 0;

	czeitb = czeita + 4*pulsx;
	czeitc = czeita + (7*pulsx);
	czeitd = czeita + (9*pulsx);
	czeite = czeita + (13*pulsx);
	czeitf = czeita + (17*pulsx);
	czeitg = czeita + (19*pulsx);
	czeith = czeita + (22*pulsx);
	czeiti = czeita + (26*pulsx); // celli dure 3 pulsx

	czeitf2 = czeita + pulsx;
	czeitg2 = czeita + (3*pulsx);
	czeith2 = czeita + (6*pulsx);
	czeiti2 = czeita + (10*pulsx);
	czeita2 = czeita + (11*pulsx);
	czeitb2 = czeita + (15*pulsx);
	czeitc2 = czeita + (18*pulsx);
	czeitd2 = czeita + (20*pulsx);
	czeite2 = czeita + (24*pulsx);

	zeitzi1 = czeita + (2*pulsx);
	zeitzi2 = czeita + (5*pulsx);
	zeitzi3 = czeita + (8*pulsx);
	zeitzi4 = czeita + (12*pulsx);
	zeitzi5 = czeita + (14*pulsx);
	zeitzi6 = czeita + (16*pulsx);
	zeitzi7 = czeita + (21*pulsx);
	zeitzi8 = czeita + (23*pulsx);
	zeitzi9 = czeita + (25*pulsx);

	// duree canon 3 : 29*pulsx

	//-----Cella : montee descente
	cella = {
		var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
		lha = min((max(lh5, 2)),5); coeff = min ((fmx1/fmx3), (fmx3/fmx1));
		dura1 = 2*pulsx*(lha/10); dura2 = 2*pulsx*((10-lha)/10);  //duree locales entre debut et fin de cellule a deborde sur f2 durx2

		cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.05*coeff/dura1)*55*rampFunc)), \len, (0.015+((0.05*coeff/dura1)*0.35*rampFunc))*coeff,
			\dur, Pseq([0.05*coeff], dura1/(0.05*coeff)), \pan, ((0.7*sinFunc)-(0.8*cosFunc))*((0.05*coeff/dura1)*10*rampFunc) +0.6, \vol, ((0.5*sinFunc)+(0.7*cosFunc))*((0.05*coeff/dura1)*7*rampFunc), \envIdx, 1);
		/*  cap2 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[40, 550, 2300, 1800], inf), \filtQ, 0.6, \bufNum, soundFile1.bufnum, \start, t2 + (-0.10*sinFunc)-(0.15*cosFunc), \speed, (0+((0.033*coeff/dura1)*20*rampFunc)), \len, (0.015+((0.033*coeff/dura1)*0.18*rampFunc))*coeff,\dur, Pseq([0.033*coeff], dura1/(0.033*coeff)), \pan, ((-0.9*sinFunc)+(0.8*cosFunc))*((0.033*coeff/dura1)*10*rampFunc) +0.6, \vol, ((-0.7*sinFunc)+(0.9*cosFunc))*((0.033*coeff/dura1)*7*rampFunc), \envIdx, 1); */
		cap3 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[1400,  1500, 600, 300, 400, 1600], inf), \filtQ, 0.8,\bufNum, soundFile1.bufnum, \start, t1 + (0.15*sinFunc)+(0.13*cosFunc), \speed,  (0.5+((0.025*coeff/dura1)*15*rampFunc)), \len, (0.015+((0.025*coeff/dura1)*0.1*rampFunc))*coeff,
			\dur, Pseq([0.025*coeff], dura1/(0.025*coeff)), \pan, ((-0.8*sinFunc)-(0.9*cosFunc))*((0.025*coeff/dura1)*10*rampFunc) +0.6, \vol, ((0.7*sinFunc)-(0.5*cosFunc))*((0.025*coeff/dura1)*7*rampFunc), \envIdx, 1);
		cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.04*coeff/dura2)*5*rampFunc)), \len, (0.035-((0.04*coeff/dura1)*0.2*rampFunc))*coeff,
			\dur, Pseq([0.04*coeff], dura2/(0.04*coeff)), \pan, ((((0.7*sinFunc)-(0.8*cosFunc))*(1.1-((0.04*coeff/dura2)*12*rampFunc))) -0.4), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(0.7-((0.04*coeff/dura2)*7*rampFunc))), \envIdx, 1);
		/* cap5 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[40, 550, 2300, 1800], inf), \filtQ, 0.6, \bufNum, soundFile1.bufnum, \start, t2 + (-0.10*sinFunc)-(0.15*cosFunc), \speed, (2-((0.03*coeff/dura2)*2*rampFunc)), \len, (0.025-((0.03*coeff/dura1)*0.1*rampFunc))*coeff,\dur, Pseq([0.03*coeff], dura2/(0.03*coeff)), \pan, ((-0.9*sinFunc)+(0.8*cosFunc))*(1.1-((0.03*coeff/dura2)*12*rampFunc)) -0.4, \vol, (((-0.7*sinFunc)+(0.9*cosFunc))*(0.7-((0.03*coeff/dura2)*7*rampFunc))), \envIdx, 1); */
		cap6 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[1400,  1500, 600, 300, 400, 1600], inf), \filtQ, 0.8,\bufNum, soundFile1.bufnum, \start, t1 + (0.15*sinFunc)+(0.13*cosFunc), \speed,  (2-((0.02*coeff/dura2)*10*rampFunc)), \len, 0.015*coeff,
			\dur, Pseq([0.02*coeff], dura2/(0.02*coeff)), \pan, ((-0.8*sinFunc)-(0.9*cosFunc))*(1.1-((0.02*coeff/dura2)*12*rampFunc)) -0.4, \vol, (((0.7*sinFunc)-(0.5*cosFunc))*(0.7-((0.02*coeff/dura2)*7*rampFunc))), \envIdx, 1);

		Ptpar([0.00, cap1, 0.00, cap3 ,
			dura1, cap4, dura1, cap6], 1);
	};

	//-----Cellf2 : modulation en anneau constante, puis de craquements a ringmod ICI INVERSE
	cellf2 = {
		var czp1, czp2, czp3, cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = (lh6/10); coeff = min((max ((l2/l6), (l6/l2))), 6); coeff2 = max((min ((l2/l6), (l6/l2))), 0.5);
		durx1 = pulsx*(lhx+1); durx2 = pulsx*(1.5-lhx);  //duree locales entre debut et fin de cellule deborde sur zitat1

		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z1+0.07*cosFunc, \speed, 0.4, \len, 0.075*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.08*coeff2], durx2/(0.08*coeff2)), \pan, ((10*rampFunc*0.08*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)) -0.4, \vol, ((10*rampFunc*0.08*coeff2)/durx2), \envIdx, 3);
		/* cxp5 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z2+0.05*sinFunc, \speed, 1, \len, 0.065*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.07*coeff2], durx2/(0.07*coeff2)), \pan, ((10*rampFunc*0.07*coeff2)/durx2)*(0.7*(cosFunc-sinFunc)) -0.4, \vol, ((10*rampFunc*0.07*coeff2)/durx2), \envIdx, 3); */
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z3-0.06*cosFunc, \speed, 0.7, \len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.06*coeff2], durx2/(0.06*coeff2)), \pan, ((10*rampFunc*0.06*coeff2)/durx2)*(-0.7*(cosFunc-sinFunc)) -0.4, \vol, ((10*rampFunc*0.06*coeff2)/durx2), \envIdx, 3);
		cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z1+0.07*cosFunc, \speed, 0.8, \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.06*coeff2)/durx1))), \dur, Pseq([0.08*coeff], durx1/(0.08*coeff)), \pan,(1-((10*rampFunc*0.08*coeff2)/durx1))*((0.7*(cosFunc+sinFunc))-1)+0.8, \vol, 1-((10*rampFunc*0.08*coeff)/durx1), \envIdx, 3);
		/* cxp2 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z2+0.05*sinFunc, \speed, 1.2, \len, 0.065*(coeff+((coeff2-coeff)*((10*rampFunc*0.06*coeff2)/durx1))), \dur, Pseq([0.07*coeff], durx1/(0.07*coeff)), \pan, (1-((10*rampFunc*0.07*coeff2)/durx1))*((0.7*(cosFunc-sinFunc))-1)+0.8, \vol, 1-((10*rampFunc*0.07*coeff)/durx1), \envIdx, 3); */
		cxp3 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z3-0.06*cosFunc, \speed, 0.9, \len, 0.055*(coeff+((coeff2-coeff)*((10*rampFunc*0.06*coeff2)/durx1))), \dur, Pseq([0.06*coeff], durx1/(0.06*coeff)), \pan, ((-0.7*(cosFunc-sinFunc))-1)*(1-((10*rampFunc*0.06*coeff2)/durx1))+0.8, \vol, 1-((10*rampFunc*0.07*coeff)/durx1), \envIdx, 3);

		// citations
		czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmx1, \speed, (l1/l2),\len, (pulsx*0.9),\dur, Pseq([(pulsx*0.9)], 1),\pan, 0.4,\vol, ((l2/l1)/6),\envIdx, 1); //citation
		czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmax1, \speed, (l3/l4),\len, (pulsx*0.7),\dur, Pseq([(pulsx*0.7)], 1),\pan, -0.7,\vol, ((l4/l3)/4),\envIdx, 4); //citation
		czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, 0, \speed, ((-1*l5)/l6),\len, (pulsx*0.9),\dur, Pseq([(pulsx*0.9)], 1),\pan, -0.1,\vol, ((l6/l5)/5),\envIdx, 2); //citation

		Ptpar([0.00, cxp4, 0.00, cxp6 ,
			durx2, cxp1, durx2, cxp3,
			0, czp1, (pulsx*0.9), czp2, (pulsx*1.6), czp3
			], 1);
	};

	//-----zitat1 : citation
	zitat1 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
		lhx = min((lh5/10), ((10-lh5)/1));
		durx1 = 2*pulsx*lhx; durx2 = 2*pulsx*(1-lhx);  //duree va un peu sur g2

		cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile1.bufnum, \bufNum2, soundFile6.bufnum, \start, (min(t1, z1) + (0.1*(l1/l6)*cosFunc)), \speed, (fmin1/fmin6), \dur, Pseq([0.3*(l1/l6)], durx1/(0.3*(l1/l6))), \len, 0.3*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.3*(l1/l6))*rampFunc/durx1), \vol, (10*(0.3*(l1/l6))*rampFunc/durx1), \envIdx, 3);
		cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile1.bufnum, \bufNum2, soundFile6.bufnum, \start, (min(t1, z1) + (0.1*(l6/l1)*cosFunc)), \speed, (fmin1/fmin6), \dur, Pseq([0.35*(l1/l6)], durx2/(0.35*(l1/l6))), \len, 0.35*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.35*(l1/l6))*rampFunc/durx2)), \envIdx, 3);

		Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
	};

	//-----Cellg2
	cellg2 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
		coeff = (l3/l6); durx1 = 2*pulsx;  durx2 =pulsx; //duree locales entre debut et fin de cellule va sur b puis termine sur zitat2
		lx = l6;
		r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
		rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsx  ne pas diviser par 5

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),
			\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, Pseq([(r1*10), (-10*r2), (10*r3), (-10*r4), (10*r5)], inf), \vol, 0.4+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 3);
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.6*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile6.bufnum, \start, z2 + (0.10*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),
			\len, Pseq([r5, r4, r1, r3, r2], inf), \dur, Pseq([r5, r4, r1, r3, r2], durx1/rm),\pan, Pseq([(-10*r5), (10*r4), (-10*r1), (10*r3), (-10*r2)], inf), \vol, 0.4+(0.3*sinFunc)+(-0.2*cosFunc), \envIdx, 3);
		cxp5 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),
			\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx2/rm), \pan, (0.5-((7*rampFunc*(rm/5))/durx2)), \vol, (0.4-((4*rampFunc*(rm/5))/durx2)), \envIdx, 3);
		cxp6 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.6*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile6.bufnum, \start, z2 + (0.10*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),
			\len, Pseq([r5, r4, r1, r3, r2], inf), \dur, Pseq([r5, r4, r1, r3, r2], durx2/rm),\pan, (-0.5-((3*rampFunc*(rm/5))/durx2)), \vol, (0.4-((4*rampFunc*(rm/5))/durx2)), \envIdx, 3);

		Ptpar([0.00, cxp1, 0.00, cxp3,
			durx1, cxp5, durx1, cxp6 ], 1);
	};

	//-----Cellb : constante mouillee montee
	cellb = {
		var czp1, czp2, czp3, cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
		lhb = lh4; coeff = max ((fmin1/fmin3), (fmin3/fmin1));
		durb1 = pulsx*(lhb/10); durb2 = pulsx*((10-lhb)/10);  //duree locales entre debut et fin de cellule

		cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.020*coeff,
			\dur, Pseq([0.020*coeff], durb1/(0.020*coeff)), \pan, (-1+((14*rampFunc*0.020*coeff)/durb1)), \vol, 0.5+(-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
		cbp3 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[250, 500, 100, 200], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t3 + (0.10*sinFunc)-(0.10*cosFunc), \speed, 0.8, \len, 0.010*coeff,
			\dur, Pseq([0.010*coeff], durb1/(0.010*coeff)), \pan, (-1+((14*rampFunc*0.010*coeff)/durb1)), \vol, 0.5+(0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
		cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.020*coeff)/durb2), \len, 0.020*coeff,
			\dur, Pseq([0.020*coeff], durb2/(0.020*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.020*coeff)/durb2))), \vol, (0.5+(-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.020*coeff)/durb2)), \envIdx, 1);
		cbp6 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[250, 500, 100, 200], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t3 + (0.10*sinFunc)-(0.10*cosFunc), \speed, 0.8+((15*rampFunc*0.010*coeff)/durb2), \len, 0.010*coeff,
			\dur, Pseq([0.010*coeff], durb2/(0.010*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.010*coeff)/durb2))), \vol, (0.5+(0.7*sinFunc)-(0.5*cosFunc))/(1+((100*rampFunc*0.010*coeff)/durb2)), \envIdx, 1);

		// citations
		czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmax2, \speed, (l2/l1),\len, (pulsx*0.4),\dur, Pseq([(pulsx*0.4)], 1),\pan, -0.4,\vol, ((l1/l2)/8),\envIdx, 1); //citation
		czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmx2, \speed, ((-1*l4)/l3),\len, (pulsx*0.3),\dur, Pseq([(pulsx*0.3)], 1),\pan, 0.2,\vol, ((l3/l4)/8),\envIdx, 4); //citation
		czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmx2, \speed, (l6/l5),\len, (pulsx*0.2),\dur, Pseq([(pulsx*0.2)], 1),\pan, 0.1,\vol, ((l5/l6)/8),\envIdx, 2); //citation


		Ptpar([0.00, cbp1, 0.00, cbp3 ,
			durb1, cbp4, durb1, cbp6,
			0, czp1, (pulsx*0.4), czp2, (pulsx*0.7), czp3
			], 1);
	};

	//-----zitat2 : citation
	zitat2 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, durx3, lhx, coeff, coeff2;
		lhx = (lh6/10); coeff = max((min ((l2/(2*l6)), (l6/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l6), (((2*l6)/l2)))), 1.5);
		durx1 = pulsx*lhx; durx2 = pulsx*(1-lhx);  //duree va un peu sur g2
		durx3 = pulsx;  //duree
		// joue a la frequence max (fmin1, fmax6)

		// zitat
		cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, (max(t1, z1) + (0.1*(l6/l1)*cosFunc)), \speed, (fmin6/fmin1), \dur, Pseq([0.3*(l1/l6)], durx1/(0.3*(l1/l6))), \len, 0.3*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.3*(l1/l6))*rampFunc/durx1), \vol, (10*(0.3*(l1/l6))*rampFunc/durx1), \envIdx, 3);
		cxp2 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, (max(t1, z1) + (0.1*(l1/l6)*cosFunc)), \speed, (fmin6/fmin1), \dur, Pseq([0.35*(l1/l6)], durx2/(0.35*(l1/l6))), \len, 0.35*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (0.8-(10*(0.35*(l1/l6))*rampFunc/durx2)), \envIdx, 3);

		// fade in de craquement pour cellh2
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1900 +(600*squared(0.6*cosFunc))+(400*sinFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 3.5+(-0.02*sinFunc)+(0.01*cosFunc),
			\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.025*coeff], durx3/(0.027*coeff)), \pan, 0.5+((-13*rampFunc*0.027*coeff)/durx3), \vol, ((5*rampFunc*0.027*coeff)/durx3), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 1700 +(1000*squared(0.5*sinFunc))-(700*sinFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 3.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, (1.01-(cos((-1)*compFunc1)))*(coeff/3), \dur, Pseq([0.027*coeff], durx3/(0.025*coeff)), \pan, 0.5+((-2*rampFunc*0.025*coeff)/durx3), \vol, ((6*rampFunc*0.025*coeff)/durx3), \envIdx, 1);

		Ptpar([0.00, cxp1, durx1, cxp2, 0.00, cxp2, 0.00, cxp4 ], 1);
	};

	//-----Cellh2 : craquements constants puis vers ringmod
	cellh2 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = (lh6/10); coeff = max((min ((l2/(2*l6)), (l6/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l6), (((2*l6)/l2)))), 1.5);
		durx1 = pulsx*lhx; durx2 = pulsx*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp2 = Pbind(\instrument, \instRLPF, \filtFrq, 1900 +(600*squared(0.6*cosFunc))+(400*sinFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 3.5+(-0.02*sinFunc)+(0.01*cosFunc),
			\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.025*coeff], durx1/(0.027*coeff)), \pan, (-0.8+((15*rampFunc*0.027*coeff)/durx1)), \vol, 0.5+(0.2*sinFunc)+(-0.1*cosFunc), \envIdx, 1);
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1700 +(1000*squared(0.5*sinFunc))-(700*sinFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 3.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, (1.01-(cos((-1)*compFunc1)))*(coeff/3), \dur, Pseq([0.027*coeff], durx1/(0.025*coeff)), \pan, (0.3-((4*rampFunc*0.025*coeff)/durx1)), \vol, 0.6+(0.5*sinFunc)+(0.2*cosFunc), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);
		cxp5 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 3.5+(-0.02*sinFunc)+(0.01*cosFunc),
			\len, 0.065*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff2)/durx2))), \dur, Pseq([0.025*coeff2], durx2/(0.025*coeff2)), \pan, (1-((10*rampFunc*0.027*coeff2)/durx2))*(0.7*sinFunc), \vol, 1, \envIdx, 1);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 3.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.055*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff2)/durx2))), \dur, Pseq([0.027*coeff2], durx2/(0.027*coeff2)), \pan, (1-((10*rampFunc*0.025*coeff2)/durx2))*(-0.1*sinFunc), \vol, 1, \envIdx, 1);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3,
			durx1, cxp4, durx1, cxp5, durx1, cxp6], 1);
	};

	//-----Cellc : constante transposee
	cellc = {
		var czp1, czp2, czp3, cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

		lhx = 0.1*(max(lh3, (1-lh3))); coeff = max((min ((fmax1/fmax5), (fmax5/fmax1))), 0.5);
		durx1 = pulsx*lhx; durx2 = pulsx*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, ((10*rampFunc*0.20*coeff)/durx1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp2 = Pbind(\instrument, \instRLPF, \filtFrq, 1500 +(1000*squared(0.5*cosFunc))+(650*cosFunc), \filtQ, 0.6, \bufNum, soundFile1.bufnum, \start, t1 + (-0.08*sinFunc)+(0.14*cosFunc), \speed, 3.5+(-0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.15*coeff, \dur, Pseq([0.15*coeff], durx1/(0.15*coeff)), \pan, ((8*rampFunc*0.15*coeff)/durx1), \vol, 0.6+(0.3*sinFunc)-(0.2*cosFunc), \envIdx, 1);
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 3.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.10*coeff, \dur, Pseq([0.10*coeff], durx1/(0.10*coeff)), \pan,((9*rampFunc*0.10*coeff)/durx1),  \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp5 = Pbind(\instrument, \instRLPF, \filtFrq, 1500 +(1000*squared(0.5*cosFunc))+(650*cosFunc), \filtQ, 0.6, \bufNum, soundFile1.bufnum, \start, t2 + (-0.08*sinFunc)+(0.14*cosFunc), \speed, (3.5*(fmin1/fmin5))+(-0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.15*coeff, \dur, Pseq([0.15*coeff], durx2/(0.15*coeff)), \pan, Pseq(#[0, -1, 1], inf), \vol, 0.6+(0.3*sinFunc)-(0.2*cosFunc), \envIdx, 1);
		cxp6 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile1.bufnum, \start, t2 + (0.08*sinFunc)+(0.09*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.10*coeff, \dur, Pseq([0.10*coeff], durx2/(0.10*coeff)), \pan, Pseq(#[-1, 0, 1], inf), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 1);

		// citations
		czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmx3, \speed, (-1*l1/l3),\len, (pulsx*0.3),\dur, Pseq([(pulsx*0.3)], 1),\pan, -0.4,\vol, ((l3/l1)/8),\envIdx, 1); //citation
		czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, 0, \speed, (l3/l5),\len, (pulsx*0.4),\dur, Pseq([(pulsx*0.4)], 1),\pan, 0.3,\vol, ((l5/l3)/7),\envIdx, 4); //citation
		czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmax3, \speed, (-1*l5/l1),\len, (pulsx*0.3),\dur, Pseq([(pulsx*0.3)], 1),\pan, -0.8,\vol, ((l1/l5)/6),\envIdx, 2); //citation


		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6,
			0, czp1, (pulsx*0.3), czp2, (pulsx*0.7), czp3
			], 1);
	};

	//-----zitat3 : citation + ringmod a craquement
	zitat3 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, durx3, lhx, coeff, coeff2;
		lhx = (lh4/10); coeff = max((min ((l2/(2*l6)), (l6/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l6), (((2*l6)/l2)))), 1.5);
		durx1 = pulsx*lhx; durx2 = pulsx*(1-lhx); durx3 = pulsx*2 ;  //duree locales entre debut et fin de cellule craquement dure 2 puls

		// zitat
		cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, ((t1+ z1)/2) + (0.1*(l1/l6)*cosFunc), \speed, 1, \dur, Pseq([0.3*(l1/l6)], durx1/(0.3*(l1/l6))), \len, 0.3*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.3*(l1/l6))*rampFunc/durx1), \vol, (10*(0.3*(l1/l6))*rampFunc/durx1), \envIdx, 3);
		cxp2 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, ((t1+ z1)/2) + (0.1*(l6/l1)*cosFunc), \speed, 1, \dur, Pseq([0.35*(l1/l6)], durx2/(0.35*(l1/l6))), \len, 0.35*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (0.8-(10*(0.35*(l1/l6))*rampFunc/durx2)), \envIdx, 3);

		// ringmod a craquement
		cxp5 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t3 + (0.08*sinFunc)+(0.09*cosFunc), \speed, 3.5+(-0.02*sinFunc)+(0.01*cosFunc),
			\len, 0.065*(coeff2+((coeff-coeff2)*((10*rampFunc*0.025*coeff2)/durx3))), \dur, Pseq([0.025*coeff2], durx3/(0.025*coeff2)), \pan, (0+((-3*rampFunc*0.027*coeff2)/durx3))*(0.7*sinFunc), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.025*coeff2)/durx3))), \dur, Pseq([0.027*coeff2], durx3/(0.027*coeff2)), \pan, (1-((7*rampFunc*0.025*coeff2)/durx3))*(-0.1*sinFunc), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

		Ptpar([0.00, cxp1, durx1, cxp2,
			0.00, cxp5, 0.00, cxp6  ], 1);

	};

	//-----Celld : craquements plus ou moins resonants
	celld = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
		lhx = (lh2/10); coeff = min((l1/l3), (l3/l1));
		durx1 = (pulsx/3); durx2 = pulsx*((2/3)+17); //: dure jusqu a fin

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, 0.3, \vol, (0.2+(-0.2*sinFunc)+(0.1*cosFunc))*((10*rampFunc*0.03*coeff)/durx1), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx2/(0.03*coeff)), \pan, (0.3-((8*rampFunc*0.03*coeff)/durx2))-compFunc1, \vol, 0.2+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

		Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
	};

	//-----Celli2 : constante transposee
	celli2 = {
		var czp1, czp2, czp3, cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

		lhx = min((lh4/10), ((10-lh4)/10)); coeff = min((max ((l3/l6), (l6/l3))), 1.5); coeff2 = max((min ((l3/l6), (l6/l3))), 0.5);
		durx1 = pulsx*lhx; durx2 = pulsx*(1.6-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff)/durx1))), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, ((18*rampFunc*0.03*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, ((10*rampFunc*0.03*coeff)/durx1), \envIdx, 1);
		cxp3 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 2.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.055*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff)/durx1))), \dur, Pseq([0.027*coeff], durx1/(0.027*coeff)), \pan, ((18*rampFunc*0.025*coeff)/durx1)-0.8+(-0.1*cosFunc), \vol, ((10*rampFunc*0.025*coeff)/durx1), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (2.5-((20*rampFunc*0.035*coeff2)/durx2)+(0.01*sinFunc)-(0.01*cosFunc)),
			\len, 0.075*(coeff2+((coeff-coeff2)*((10*rampFunc*0.035*coeff2)/durx2))), \dur, Pseq([0.035*coeff2], durx2/(0.035*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.035*coeff2)/durx2)), \envIdx, 1);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, (2.5-((27*rampFunc*0.032*coeff2)/durx2)+(0.01*sinFunc)+(0.01*cosFunc)),
			\len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.032*coeff2)/durx2))), \dur, Pseq([0.032*coeff2], durx2/(0.032*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.032*coeff2)/durx2)), \envIdx, 1);

		// citations
		czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmax3, \speed, (l2/l4),\len, (pulsx*0.7),\dur, Pseq([(pulsx*0.7)], 1),\pan, 0.2,\vol, ((l4/l2)/7),\envIdx, 2); //citation
		czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, 0, \speed, (l4/l6),\len, (pulsx*0.5),\dur, Pseq([(pulsx*0.5)], 1),\pan, 0.7,\vol, ((l6/l4)/8),\envIdx, 1); //citation
		czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmx3, \speed, (l5/l1),\len, (pulsx*0.3),\dur, Pseq([(pulsx*0.3)], 1),\pan, -0.8,\vol, ((l1/l6)/7),\envIdx, 1); //citation

		Ptpar([0.00, cxp1, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp6,
			0, czp1, (pulsx*0.7), czp2, (pulsx*1.2), czp3
			], 1);
	};

	//-----Cella2 : montee descente
	cella2 = {
		var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff, coeff2;
		coeff = max((min ((l3/l6), (l6/l3))), 0.5); coeff2 = min((max ((l3/l6), (l6/l3))), 1.5);
		dura1 = pulsx*0.6; dura2 = pulsx*0.4;  //duree locales entre debut et fin de cellule

		cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile6.bufnum, \start, z3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, (-3+((0.055*coeff/dura1)*55*rampFunc)),
			\len, (0.055*coeff),\dur, Pseq([0.055*coeff], dura1/(0.055*coeff)), \pan, (cos((0.1)*sqrt(exp(50-rampFunc)))), \vol, ((0.5*sinFunc)+(0.7*cosFunc))*((0.055*coeff/dura1)*10*rampFunc), \envIdx, 1);
		cap3 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[1400,  1500, 600, 300, 400, 1600], inf), \filtQ, 0.8,\bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed,  (0.5+((0.075*coeff/dura1)*15*rampFunc)),
			\len, (0.075*coeff) , \dur, Pseq([0.075*coeff], dura1/(0.075*coeff)), \pan, (cos((0.1)*sqrt(exp(50-rampFunc)))), \vol, ((0.7*sinFunc)-(0.5*cosFunc))*((0.075*coeff/dura1)*10*rampFunc), \envIdx, 1);
		cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile6.bufnum, \start, z3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, (2.5-((0.05*coeff/dura2)*5*rampFunc)),
			\len, (0.050*coeff),\dur, Pseq([0.05*coeff], dura2/(0.05*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.05*coeff/dura2)*12*rampFunc))), \envIdx, 1);
		cap6 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[1400,  1500, 600, 300, 400, 1600], inf), \filtQ, 0.8,\bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed,  (2-((0.07*coeff/dura2)*10*rampFunc)),
			\len, 0.070*coeff, \dur, Pseq([0.07*coeff], dura2/(0.07*coeff)), \pan, (-0.8*sinFunc)-(0.9*cosFunc), \vol, (((0.7*sinFunc)-(0.5*cosFunc))*(1.1-((0.07*coeff/dura2)*12*rampFunc))), \envIdx, 1);

		Ptpar([0.00, cap1, 0.00, cap2, 0.00, cap3 ,
			dura1, cap4, dura1, cap5, dura1, cap6], 1);
	};

	//-----zitat4 : citation
	zitat4 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = (lh3/10);
		durx1 = 2*pulsx*lhx; durx2 = 2*pulsx*(2-lhx ) ;  //duree locales entre debut et fin de cellule craquement dure 2 puls

		cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, (min(t2, z2) + (0.1*(l1/l6)*cosFunc)), \speed, ((fmin1/fmin6)+0.5), \dur, Pseq([0.25*(l1/l6)], durx1/(0.25*(l1/l6))), \len, 0.25*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.25*(l1/l6))*rampFunc/durx1), \vol, (10*(0.25*(l1/l6))*rampFunc/durx1), \envIdx, 3);
		cxp2 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, (min(t2, z2) + (0.1*(l1/l6)*cosFunc)), \speed, ((fmin1/fmin6)+0.5), \dur, Pseq([0.3*(l1/l6)], durx2/(0.3*(l1/l6))), \len, 0.3*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (cos((0.1)*sqrt(exp(5*rampFunc)))), \vol, (0.8-(10*(0.3*(l1/l6))*rampFunc/durx2)), \envIdx, 3);

		Ptpar([0.00, cxp1, durx1, cxp2 ], 1);
	};

	//-----Celle : montee discrete
	celle = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
		lhx = max((lh1/10), ((10-lh1)/10)); coeff = min ((fmx2/fmx1), (fmx1/fmx2));
		durx1 = 2*pulsx*lhx; durx2 = 2*pulsx*(1-lhx);  //duree locales entre debut et fin de cellule deborde sur zitat5

		cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, t3+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.5+((5*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
		cxp3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, t1-0.7*sinFunc, \speed, 1.8+((13*rampFunc*0.190*coeff)/durx1), \len, 0.185*coeff,\freq, fmx5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx1/(0.190*coeff)), \pan, (-1)*(cos(exp((0.3)*rampFunc))), \vol,  0.5+((5*rampFunc*0.190*coeff)/durx1), \envIdx, 5);
		cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, t3+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((10*rampFunc*0.2*coeff)/durx2), \envIdx, 5);
		cxp6 = Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, t1-0.7*sinFunc, \speed, 3.1, \len, 0.185*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmx5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx2/(0.190*coeff)), \pan, (0.7*sinFunc)+(0.8*cosFunc), \vol,  1-((10*rampFunc*0.190*coeff)/durx2), \envIdx, 5);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};

	//-----zitat5 : citation
	zitat5 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
		lhx = (lh2/10);
		durx1 = pulsx*lhx; durx2 = pulsx*(1-lhx ) ;

		cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile1.bufnum, \bufNum2, soundFile6.bufnum, \start, (max(t2, z2) + (0.1*(l6/l1)*cosFunc)), \speed, ((fmin6/fmin1)+0.5), \dur, Pseq([0.25*(l1/l6)], durx1/(0.25*(l1/l6))), \len, 0.25*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc)), \vol, (10*(0.25*(l1/l6))*rampFunc/durx1), \envIdx, 3);
		cxp2 = Pbind( \instrument, \instXFade, \bufNum1, soundFile1.bufnum, \bufNum2, soundFile6.bufnum, \start, (max(t2, z2) + (0.1*(l1/l6)*cosFunc)), \speed, ((fmin6/fmin1)+0.5), \dur, Pseq([0.3*(l1/l6)], durx2/(0.3*(l1/l6))), \len, 0.3*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (-0.5+ (0.5*sinFunc)+(-0.7*cosFunc))*(1-(10*(0.25*(l1/l6))*rampFunc/durx1))+0.5, \vol, (1-(10*(0.3*(l1/l6))*rampFunc/durx2)), \envIdx, 3);

		Ptpar([0.00, cxp1, durx1, cxp2  ], 1);
	};

	//-----Cellb2 : constante mouillee montee
	cellb2 = {
		var czp1, czp2, czp3, cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
		lhb = lh4; coeff = max((min ((fmin1/fmin6), (fmin6/fmin1))), 0.5);
		durb1 = pulsx*(lhb/10); durb2 = pulsx*((10-lhb)/10);  //duree locales entre debut et fin de cellule

		cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.10*coeff,
			\dur, Pseq([0.10*coeff], durb1/(0.10*coeff)), \pan, (-1+((14*rampFunc*0.10*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
		cbp3 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[250, 500, 100, 200], inf), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z3 + (0.08*sinFunc)-(0.09*cosFunc), \speed, 0.5, \len, 0.05*coeff,
			\dur, Pseq([0.05*coeff], durb1/(0.05*coeff)), \pan, (-1+((14*rampFunc*0.05*coeff)/durb1)), \vol, (0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
		cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.10*coeff)/durb2), \len, 0.10*coeff,
			\dur, Pseq([0.10*coeff], durb2/(0.10*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.20*cosFunc))*((10*rampFunc*0.10*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.10*coeff)/durb2)), \envIdx, 1);
		cbp6 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[250, 500, 100, 200], inf), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z3 + (0.10*sinFunc)-(0.09*cosFunc), \speed, 0.5+((15*rampFunc*0.05*coeff)/durb2), \len, 0.05*coeff,
			\dur, Pseq([0.05*coeff], durb2/(0.05*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.05*coeff)/durb2))), \vol, ((0.7*sinFunc)-(0.5*cosFunc))/(1+((100*rampFunc*0.05*coeff)/durb2)), \envIdx, 1);

		// citations
		czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, 0, \speed, (l1/l6),\len, (pulsx*0.2),\dur, Pseq([(pulsx*0.2)], 1),\pan, 0.4,\vol, ((l2/l4)/6),\envIdx, 2); //citation
		czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmx6, \speed, (-1*l3/l6),\len, (pulsx*0.3),\dur, Pseq([(pulsx*0.3)], 1),\pan, -0.7,\vol, ((l4/l2)/6),\envIdx, 4); //citation
		czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmx6, \speed, (l2/l6),\len, (pulsx*0.5),\dur, Pseq([(pulsx*0.5)], 1),\pan, -0.1,\vol, ((l6/l2)/7),\envIdx, 1); //citation

		Ptpar([0.00, cbp1, 0.00, cbp2, 0.00, cbp3 ,
			durb1, cbp4, durb1, cbp5, durb1, cbp6,
			0, czp1, (pulsx*0.2), czp2, (pulsx*0.5), czp3
			], 1);
	};

	//-----zitat6 : citation + ringmod a craquement
	zitat6 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = (lh6/10); coeff = max((min ((l2/(2*l6)), (l6/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l6), (((2*l6)/l2)))), 1.5);
		durx1 = pulsx*lhx; durx2 = pulsx*(2-lhx ) ;
		//duree locales entre debut et fin de cellule craquement dure 2 puls

		// Rien (gresillement seul)

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3,
			durx1, cxp5, durx1, cxp6  ], 1);
	};

	//-----Cellf : modulation en anneau constante, puis de craquements a ringmod Inverse
	cellf = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = min((lh2/10), ((10-lh2)/10)); coeff = min((max ((l2/l1), (l1/l2))), 1.5); coeff2 = max((min ((l2/l1), (l1/l2))), 0.5);
		durx1 = 2*pulsx*lhx; durx2 = 2*pulsx*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t1+0.07*cosFunc, \speed, 0.4, \len, 0.075*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.08*coeff2], durx2/(0.08*coeff2)), \pan, ((10*rampFunc*0.08*coeff2)/durx2)*(0.7+(cosFunc-sinFunc)), \vol, ((10*rampFunc*0.08*coeff)/durx2), \envIdx, 3);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t3-0.06*cosFunc, \speed, 0.7, \len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.06*coeff2)/durx2))), \dur, Pseq([0.06*coeff2], durx2/(0.06*coeff2)), \pan, ((10*rampFunc*0.06*coeff2)/durx2)*(0.7+(sinFunc-cosFunc)), \vol, ((10*rampFunc*0.06*coeff)/durx2), \envIdx, 3);
		cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t1+0.07*cosFunc, \speed, 0.8, \len, 0.075*coeff, \dur, Pseq([0.08*coeff], durx1/(0.08*coeff)), \pan, 0.7-((17*rampFunc*0.08*coeff)/durx1), \vol, 1-((10*rampFunc*0.08*coeff)/durx1), \envIdx, 3);
		cxp3 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t3-0.06*cosFunc, \speed, 0.9, \len, 0.055*coeff, \dur, Pseq([0.06*coeff], durx1/(0.06*coeff)), \pan, 0.7-((17*rampFunc*0.07*coeff)/durx1), \vol, 1-((10*rampFunc*0.07*coeff)/durx1), \envIdx, 3);

		Ptpar([0.00, cxp4, 0.00, cxp5, 0.00, cxp6 ,
			durx1, cxp1, durx1, cxp2, durx1, cxp3  ], 1);
	};

	//-----Cellc2 : constante transposee
	cellc2 = {
		var czp1, czp2, czp3, cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

		lhx = 0.1*(max(lh3, (1-lh3))); coeff = max((min ((fmax1/fmax6), (fmax6/fmax1))), 0.5);
		durx1 = pulsx*lhx; durx2 = pulsx*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)), \vol, (0.6+(-0.2*sinFunc)+(0.1*cosFunc))*(0.4+((6*rampFunc*0.20*coeff)/durx1)), \envIdx, 1);
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile6.bufnum, \start, z1 + (0.12*sinFunc)+(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.10*coeff, \dur, Pseq([0.10*coeff], durx1/(0.10*coeff)), \pan, (0.8-((10*rampFunc*0.10*coeff)/durx1)), \vol, (0.6+(0.2*sinFunc)+(0.2*cosFunc))*(0.4+((6*rampFunc*0.10*coeff)/durx1)), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp6 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile6.bufnum, \start, z2 + (0.12*sinFunc)+(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.10*coeff, \dur, Pseq([0.10*coeff], durx2/(0.10*coeff)), \pan, Pseq(#[-1, 0, 1], inf), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 1);

		// citations
		czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, tmx6, \speed, (l3/l1),\len, (pulsx*0.5),\dur, Pseq([(pulsx*0.5)], 1),\pan, -0.4,\vol, ((l2/l3)/7),\envIdx, 1); //citation
		czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, 0, \speed, (l2/l4),\len, (pulsx*0.4),\dur, Pseq([(pulsx*0.4)], 1),\pan, 0.7,\vol, ((l4/l5)/6),\envIdx, 4); //citation
		czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, tmax6, \speed, (l5/l1),\len, (pulsx*0.1),\dur, Pseq([(pulsx*0.1)], 1),\pan, 0.1,\vol, ((l6/l4)/6),\envIdx, 4); //citation


		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6,
			0, czp1, (pulsx*0.5), czp2, (pulsx*0.9), czp3
			], 1);
	};

	//-----Cellg
	cellg = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
		coeff = (l3/l1); durx1 = 2*pulsx;   //duree locales entre debut et fin de cellule
		lx = l1;
		r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
		rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsx  ne pas diviser par 5

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),
			\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (-0.8+((4*rampFunc*rm)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 3);
		cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile1.bufnum, \start, t2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),
			\len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (-0.8+compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 3);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ], 1);
	};

	//-----Celld2 : craquements plus ou moins resonants
	celld2 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
		lhx = (lh2/10); coeff = min((l1/l6), (l6/l1));
		durx1 = 6*pulsx; //: VA JUSQUA FIN AVEC D

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.3+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

		Ptpar([0.00, cxp1 ], 1);
	};

	//-----zitat7 : citation + ringmod a craquement
	zitat7 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = (lh1/10);
		durx1 = pulsx*lhx; durx2 = pulsx*(2-lhx ) ;  //duree locales entre debut et fin de cellule

		cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, (min(t3, z3) + (0.1*(l1/l6)*cosFunc)), \speed, ((fmin1/fmin6)+1), \dur, Pseq([0.20*(l1/l6)], durx1/(0.2*(l1/l6))), \len, 0.2*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, -1+ (1+(-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.25*(l1/l6))*rampFunc/durx1), \vol, (10*(0.25*(l1/l6))*rampFunc/durx1), \envIdx, 3);
		cxp2 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, (min(t3, z3) + (0.1*(l6/l1)*cosFunc)), \speed, ((fmin1/fmin6)+1), \dur, Pseq([0.25*(l1/l6)], durx2/(0.25*(l1/l6))), \len, 0.25*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (cos((0.1)*sqrt(exp(5*rampFunc)))), \vol, (1-(10*(0.3*(l1/l6))*rampFunc/durx2)), \envIdx, 3);

		Ptpar([0.00, cxp1, durx1, cxp2 ], 1);
	};

	//-----Cellh : craquements constants puis vers ringmod
	cellh = {
		var  czp1, czp2, czp3, cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
		lhx = (lh6/10); coeff = max((min ((l2/(2*l1)), (l1/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l1), (((2*l1)/l2)))), 1.5);
		durx1 = pulsx*lhx; durx2 = pulsx*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, ((-0.4*sinFunc)+(0.7*cosFunc)), \vol, 1, \envIdx, 1);

		// citations
		czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmax3, \speed, (l4/l2),\len, (pulsx*0.3),\dur, Pseq([(pulsx*0.3)], 1),\pan, 0.4,\vol, ((l2/l5)/6),\envIdx, 2); //citation
		czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmx2, \speed, (-1*l3/l5),\len, (pulsx*0.3),\dur, Pseq([(pulsx*0.3)], 1),\pan, -0.7,\vol, ((l4/l3)/5),\envIdx, 1); //citation
		czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmax4, \speed, (l5/l1),\len, (pulsx*0.4),\dur, Pseq([(pulsx*0.4)], 1),\pan, -0.1,\vol, ((l6/l2)/5),\envIdx, 4); //citation


		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3,
			durx1, cxp4, durx1, cxp5, durx1, cxp6,
			0, czp1, (pulsx*0.3), czp2, (pulsx*0.6), czp3
			], 1);
	};

	//-----zitat8 : citation + ringmod a craquement
	zitat8 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
		lhx = (lh2/10);
		durx1 = pulsx*lhx; durx2 = pulsx*(1-lhx ) ;

		cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile1.bufnum, \bufNum2, soundFile6.bufnum, \start, (max(t3, z3) + (0.1*(l6/l1)*cosFunc)), \speed, ((fmin6/fmin1)+1), \dur, Pseq([0.2*(l1/l6)], durx1/(0.2*(l1/l6))), \len, 0.2*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc)), \vol, 1, \envIdx, 3);
		cxp2 = Pbind( \instrument, \instXFade, \bufNum1, soundFile1.bufnum, \bufNum2, soundFile6.bufnum, \start, (max(t3, z3) + (0.1*(l1/l6)*cosFunc)), \speed, ((fmin6/fmin1)+1), \dur, Pseq([0.25*(l1/l6)], durx2/(0.25*(l1/l6))), \len, 0.25*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((0.5*sinFunc)+(-0.7*cosFunc))*(1-(10*(0.25*(l1/l6))*rampFunc/durx1)), \vol, (1-(11*(0.25*(l1/l6))*rampFunc/durx2)), \envIdx, 3);

		Ptpar([0.00, cxp1, durx1, cxp2  ], 1);
	};

	//-----Celle2 : montee discrete
	celle2 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
		lhx = max((lh1/10), ((10-lh1)/10)); coeff = min ((fmx2/fmx6), (fmx6/fmx2));
		durx1 = pulsx*lhx; durx2 = pulsx*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t3+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, -0.7+((3*rampFunc*0.2*coeff)/durx1), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 1);
		cxp3 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t1-0.7*sinFunc, \speed, 1.8+((13*rampFunc*0.190*coeff)/durx1), \len, 0.185*coeff,\freq, fmx5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx1/(0.190*coeff)), \pan, 0.7+((3*rampFunc*0.190*coeff)/durx1), \vol,  0.7+((3*rampFunc*0.190*coeff)/durx1), \envIdx, 1);
		cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t3+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, ((10*rampFunc*0.2*coeff)/durx2), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 1);
		cxp6 = Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, t1-0.7*sinFunc, \speed, 3.1, \len, 0.185*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmx5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.190*coeff], durx2/(0.190*coeff)), \pan, ((10*rampFunc*0.190*coeff)/durx2), \vol,  1-((6*rampFunc*0.190*coeff)/durx2), \envIdx, 1);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};

	//-----zitat9 : citation
	zitat9 = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
		lhx = (lh4/10);
		durx1 = 2*pulsx*lhx; durx2 = 2*pulsx*(1-lhx ) ;

		cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, ((t3+ z3)/2) + (0.1*(l1/l6)*cosFunc), \speed, 2, \dur, Pseq([0.2*(l1/l6)], durx1/(0.2*(l1/l6))), \len, 0.2*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, -1+(1+(-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.2*(l1/l6))*rampFunc/durx1), \vol, (10*(0.2*(l1/l6))*rampFunc/durx1), \envIdx, 3);
		cxp2 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, ((t3+ z3)/2) + (0.1*(l6/l1)*cosFunc), \speed, 2, \dur, Pseq([0.25*(l1/l6)], durx2/(0.25*(l1/l6))), \len, 0.25*(l1/l6), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.25*(l1/l6))*rampFunc/durx2)), \envIdx, 3);

		Ptpar([0.00, cxp1, durx1, cxp2  ], 1);
	};

	//-----Celli : constante transposee
	celli = {
		var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

		lhx = min((lh4/10), ((10-lh4)/10)); coeff = min((max ((l3/l1), (l1/l3))), 1.5); coeff2 = max((min ((l3/l1), (l1/l3))), 0.5);
		durx1 = 3*pulsx*lhx; durx2 = 3*pulsx*(1-lhx);  //duree locales entre debut et fin de cellule

		cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc),
			\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff)/durx1))), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, ((18*rampFunc*0.03*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);
		cxp3 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, 2.5+(0.01*sinFunc)+(0.01*cosFunc),
			\len, 0.055*(coeff+((coeff2-coeff)*((10*rampFunc*0.025*coeff)/durx1))), \dur, Pseq([0.027*coeff], durx1/(0.027*coeff)), \pan, ((18*rampFunc*0.025*coeff)/durx1)-0.8+(-0.1*cosFunc), \vol, 1, \envIdx, 1);
		cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (2.5-((20*rampFunc*0.035*coeff2)/durx2)+(0.01*sinFunc)-(0.01*cosFunc)),
			\len, 0.075*(coeff2+((coeff-coeff2)*((10*rampFunc*0.035*coeff2)/durx2))), \dur, Pseq([0.035*coeff2], durx2/(0.035*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.035*coeff2)/durx2)), \envIdx, 1);
		cxp6 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t3 + (-0.07*sinFunc)+(0.08*cosFunc), \speed, (2.5-((27*rampFunc*0.032*coeff2)/durx2)+(0.01*sinFunc)+(0.01*cosFunc)),
			\len, 0.055*(coeff2+((coeff-coeff2)*((10*rampFunc*0.032*coeff2)/durx2))), \dur, Pseq([0.032*coeff2], durx2/(0.032*coeff2)), \pan, (cos((0.1)*sqrt(exp(rampFunc)))), \vol, (1-((7*rampFunc*0.032*coeff2)/durx2)), \envIdx, 1);

		Ptpar([0.00, cxp1, 0.00, cxp2, 0.00, cxp3 ,
			durx1, cxp4, durx1, cxp5, durx1, cxp6  ], 1);
	};

	//-----Partitur du canon 3
	Ptpar([czeita, cella.value,
		czeitb, cellb.value,
		czeitc, cellc.value,
		czeitd, celld.value,
		czeite, celle.value, // commence a 24 et dure 1
		czeitf, cellf.value,
		czeitg, cellg.value,
		czeith, cellh.value,
		czeiti, celli.value, // commence a 26 et dure 3
		czeita2, cella2.value,
		czeitb2, cellb2.value,
		czeitc2, cellc2.value,
		czeitd2, celld2.value,
		czeite2, celle2.value,
		czeitf2, cellf2.value,
		czeitg2, cellg2.value,
		czeith2, cellh2.value,
		czeiti2, celli2.value,
		zeitzi1, zitat1.value,
		zeitzi2, zitat2.value,
		zeitzi3, zitat3.value,
		zeitzi4, zitat4.value,
		zeitzi5, zitat5.value,
		zeitzi6, zitat6.value,
		zeitzi7, zitat7.value,
		zeitzi8, zitat8.value,
		zeitzi9, zitat9.value // commence a 25 et dure 2
		], 1);
};

// -----------------------------------
//------------- Canon 4 --------------
// -----------------------------------

canon4= {
	var  t1, t2, t3, u1, u2, u3, v1, v2, v3, w1, w2, w3, y1, y2, y3, z1, z2, z3, //temps
	voix1, voix2, voix3, voix4, voix5, voix6, zitat, voix8,
	v1zeit, v2zeit, v3zeit, v4zeit, v5zeit, v6zeit, z1zeit, v8zeit;

	//-----Variables
	t1 = (tmin1 + tmx1)/2; t2 = (tmax1 + tmx1)/2; t3 = (tmax1 + tmin1)/2; // positions de lecture voix1 (son1)
	u1 = (tmin2 + tmx2)/2; u2 = (tmax2 + tmx2)/2; u3 = (tmax2 + tmin2)/2; // positions de lecture voix2 (son2)
	v1 = (tmin3 + tmx3)/2; v2 = (tmax3 + tmx3)/2; v3 = (tmax3 + tmin3)/2; // positions de lecture voix3 (son3)
	w1 = (tmin4 + tmx4)/2; w2 = (tmax4 + tmx4)/2; w3 = (tmax4 + tmin4)/2; // positions de lecture voix4 (son4)
	y1 = (tmin5 + tmx5)/2; y2 = (tmax5 + tmx5)/2; y3 = (tmax5 + tmin5)/2; // positions de lecture voix5 (son5)
	z1 = (tmin6 + tmx6)/2; z2 = (tmax6 + tmx6)/2; z3 = (tmax6 + tmin6)/2; // positions de lecture voix6 (son6)

	v1zeit = 0;
	v2zeit = 0;
	v3zeit = 0;
	v4zeit = 0;
	v5zeit = 0;
	v6zeit = 0;
	z1zeit = 0; // on fait demarer les 8 voix en meme temps
	v8zeit = 0; // canon longueur 134 mais petite coda voix 8 celld8 a cellf8 134 a 144, (cella � celle commencent a 100; desinence cellf a 164, cellg a 174)

	// -----------------------------------
	//------------voix 1 du canon 4 -------
	// -----------------------------------

	voix1= {

		var
		cellav, cellbv, cellcv, celldv, cellev, cellfv, cellgv, cellhv, celliv,          // voixI
		czeitav, czeitbv, czeitcv, czeitdv, czeitev, czeitfv, czeitgv, czeithv, czeitiv,
		cellaw, cellbw, cellcw, celldw, cellew, cellfw, cellgw, cellhw, celliw,
		czeitaw, czeitbw, czeitcw, czeitdw, czeitew, czeitfw, czeitgw, czeithw, czeitiw,
		cellax, cellbx, cellcx, celldx, cellex, cellfx, cellgx, cellhx, cellix,
		czeitax, czeitbx, czeitcx, czeitdx, czeitex, czeitfx, czeitgx, czeithx, czeitix,
		cellay, cellby, cellcy, celldy, celley, cellfy, cellgy, cellhy, celliy,
		czeitay, czeitby, czeitcy, czeitdy, czeitey, czeitfy, czeitgy, czeithy, czeitiy,
		cellaz, cellbz, cellcz, celldz, cellez, cellfz, cellgz, cellhz, celliz,
		czeitaz, czeitbz, czeitcz, czeitdz, czeitez, czeitfz, czeitgz, czeithz, czeitiz
		;

		//------partition du temps

		czeitav = 0;
		czeitbv = czeitav + 4*pulsz;
		czeitcv = czeitav + (7*pulsz);
		czeitdv = czeitav + (9*pulsz);
		czeitev = czeitav + (13*pulsz);
		czeitfv = czeitav + (17*pulsz);
		czeitgv = czeitav + (19*pulsz);
		czeithv = czeitav + (22*pulsz);
		czeitiv = czeitav + (26*pulsz);
		czeitaw = czeitav + (27*pulsz);
		czeitbw = czeitav + (31*pulsz);
		czeitcw = czeitav + (34*pulsz);
		czeitdw = czeitav + (36*pulsz);
		czeitew = czeitav + (40*pulsz);
		czeitfw = czeitav + (44*pulsz);
		czeitgw = czeitav + (46*pulsz);
		czeithw = czeitav + (49*pulsz);
		czeitiw = czeitav + (53*pulsz);
		czeitax = czeitav + (54*pulsz);
		czeitbx = czeitav + (58*pulsz);
		czeitcx = czeitav + (61*pulsz);
		czeitdx = czeitav + (63*pulsz);
		czeitex = czeitav + (67*pulsz);
		czeitfx = czeitav + (71*pulsz);
		czeitgx = czeitav + (73*pulsz);
		czeithx = czeitav + (76*pulsz);
		czeitix = czeitav + (80*pulsz);
		czeitay = czeitav + (81*pulsz);
		czeitby = czeitav + (85*pulsz);
		czeitcy = czeitav + (88*pulsz);
		czeitdy = czeitav + (90*pulsz);
		czeitey = czeitav + (94*pulsz);
		czeitfy = czeitav + (98*pulsz);
		czeitgy = czeitav + (100*pulsz);
		czeithy = czeitav + (103*pulsz);
		czeitiy = czeitav + (107*pulsz);
		czeitaz = czeitav + (108*pulsz);
		czeitbz = czeitav + (112*pulsz);
		czeitcz = czeitav + (115*pulsz);
		czeitdz = czeitav + (117*pulsz);
		czeitez = czeitav + (121*pulsz);
		czeitfz = czeitav + (125*pulsz);
		czeitgz = czeitav + (127*pulsz);
		czeithz = czeitav + (130*pulsz);
		czeitiz = czeitav + (145*pulsz); // canon longueur 134 mais petite coda 134 a 144 voix 8 celld8 a cellf8

		//-----Cellav : montee descente
		cellav = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh1, 2)),5); coeff = l2/l3;
			dura1 = 3*pulsz*(lha/10); dura2 = 3*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, tmax1 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff, \dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, tmax1 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff, \dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellbv : constante mouillee montee
		cellbv = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh1; coeff = l2/l3;
			durb1 = 1.5*pulsz*(lhb/10); durb2 = 1.5*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, tmx1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.10*coeff, \dur, Pseq([0.1*coeff], durb1/(0.1*coeff)), \pan, (-1+((14*rampFunc*0.10*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, tmx1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.10*coeff)/durb2), \len, 0.10*coeff, \dur, Pseq([0.1*coeff], durb2/(0.1*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.10*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.10*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4    ], 1);
		};

		//-----Cellcv : constante transposee
		cellcv = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh1, (1-lh1))); coeff = l2/l3;
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (2.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celldv : craquements plus ou moins resonants
		celldv = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3, durx1, durx2, lhx, coeff;
			lhx = (lh1/10); coeff = min((l1/l5), (l5/l1));
			durx1 = 4*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);


			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmin2, \speed, (l3/l2),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.3,\vol, ((l2/l3)/8),\envIdx, 2); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmx3, \speed, (-1*l3/l4),\len, ((durx1)/6),\dur, Pseq([((durx1)/6)], 1),\pan, 0.7,\vol, ((l4/l3)/7),\envIdx, 1); //citation
			czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, 0, \speed, (l5/l6),\len, ((durx1)/3),\dur, Pseq([((durx1)/3)], 1),\pan, -0.4,\vol, ((l6/l5)/7),\envIdx, 4); //citation


			Ptpar([0.00, cxp1, 0, czp1, ((durx1)/2), czp2, ((durx1)*(2/3)), czp3], 1);
		};

		//-----Cellev : montee discrete
		cellev = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh1/10), ((10-lh1)/10)); coeff = l2/l3;
			durx1 = pulsz*lhx; durx2 = pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmin1+(0.9*cosFunc), \speed, 0.8+((25*rampFunc*0.1*coeff)/durx1), \len, 0.095*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, (cos((0.4)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.1*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmin1+(0.9*cosFunc), \speed, 3.3, \len, 0.095*coeff-((0.5*rampFunc*0.095*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.1*coeff], durx2/(0.1*coeff)), \pan, (-0.8*sinFunc)+(0.6*cosFunc), \vol, 1-((6*rampFunc*0.1*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellfv : modulation en anneau constante, puis de craquements a ringmod
		cellfv = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t1+0.07*cosFunc, \speed, 0.8, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t1+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellgv
		cellgv = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l3/l5); durx1 = pulsz;   //duree locales entre debut et fin de cellule
			lx = l5;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2.1, 1.1, 4.1, 0.6, 2.1, 4.1, 1.1], inf), \len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile1.bufnum, \start, t2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1.1, 0.6, 2.1, 0.6, 1.1, 4.1, 2.1, 0.6, 2.1], inf), \len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhv : craquements constants puis vers ringmod
		cellhv = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = pulsz*lhx; durx2 = pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, (tmin5), \speed, ((-1)*(l3/l2)), \len, durx2, \dur, Pseq([durx2], 1),\pan, -0.6, \vol, ((l2/l3)/7),\envIdx, 2); //
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, 0, \speed, (l5/l4),\len, durx1,\dur, Pseq([durx1], 1),\pan, 0.5,\vol, ((l4/l5)/6),\envIdx, 2); //citation


			Ptpar([0.00, cxp1, durx1, cxp4, 0, czp1, durx2, czp2], 1);
		};

		//-----Celliv : constante transposee
		celliv = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh4/10), ((10-lh4)/10)); coeff = l2/l3;
			durx1 = 1.1*pulsz*lhx; durx2 = 1.1*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmin2, \speed, (l3/l2),\len, ((durx1)/(3/2)),\dur, Pseq([((durx1)/(3/2))], 1),\pan, 0.3,\vol, ((l2/l3)/7),\envIdx, 1); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmx2, \speed, (l2/l3),\len, ((durx1)/3),\dur, Pseq([((durx1)/3)], 1),\pan, 0.7,\vol, ((l3/l2)/7),\envIdx, 2); //citation


			Ptpar([0.00, cxp1, 0, czp1, ((durx1)/(3/2)), czp2 ], 1);
		};

		//-----Cellaw : montee descente
		cellaw = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh1, 2)),5); coeff = l2/l3;
			dura1 = 2*pulsz*(lha/10); dura2 = 2*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 1, \bufNum, soundFile1.bufnum, \start, tmax1 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-2.5+((0.1*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.1*coeff/dura1)*0.5*rampFunc))*coeff, \dur, Pseq([0.1*coeff], dura1/(0.1*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 1, \bufNum, soundFile1.bufnum, \start, tmax1 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.1*coeff/dura2)*5*rampFunc)), \len, (0.1-((0.1*coeff/dura1)*0.5*rampFunc))*coeff,\dur, Pseq([0.1*coeff], dura2/(0.1*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellbw : constante mouillee montee
		cellbw = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh1; coeff = (l2/l3);
			durb1 = 2*pulsz*(lhb/10); durb2 = 2*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4    ], 1);
		};

		//-----Cellcw : constante transposee
		cellcw = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh1, (1-lh1))); coeff = l2/l3;
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celldw : craquements plus ou moins resonants
		celldw = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3, durx1, durx2, lhx, coeff;
			lhx = (lh1/10); coeff = min((l1/l5), (l5/l1));
			durx1 = 4*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);


			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, 0, \speed, (l3/l1),\len, ((durx1)/3),\dur, Pseq([((durx1)/3)], 1),\pan, -0.3,\vol, ((l4/l3)/8),\envIdx, 2); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmx1, \speed, (l2/l6),\len, ((durx1)/3),\dur, Pseq([((durx1)/3)], 1),\pan, 0.2,\vol, ((l6/l3)/7),\envIdx, 3); //citation
			czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmax1, \speed, (l4/l5),\len, ((durx1)/3),\dur, Pseq([((durx1)/3)], 1),\pan, 0,\vol, ((l6/l2)/6),\envIdx, 1); //citation


			Ptpar([0.00, cxp1, 0, czp1, ((durx1)/3), czp2, ((durx1)/3), czp3 ], 1);
		};

		//-----Cellew : montee discrete
		cellew = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh1/10), ((10-lh1)/10)); coeff = l2/l3;
			durx1 = pulsz*lhx; durx2 = pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmin1+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmin1+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellfw : modulation en anneau constante, puis de craquements a ringmod inverse
		cellfw = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 2.2*pulsz*lhx; durx2 = 2.2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t3+0.07*cosFunc, \speed, 0.8, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t3+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);


			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmin4, \speed, (l3/l2),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.3,\vol, ((l1/l3)/7),\envIdx, 3); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmx4, \speed, ((-1)*(l4/l6)),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.7,\vol, ((l4/l3)/6),\envIdx, 2); //citation
			czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmx4, \speed, (l3/l5),\len, (durx2),\dur, Pseq([durx2], 1),\pan, -0.4,\vol, ((l6/l2)/7),\envIdx, 1); //citation

			Ptpar([0.00, cxp4, durx2, cxp1,  0, czp3, durx2, czp2, (((durx1)/2)+(durx2)), czp3], 1);
		};

		//-----Cellgw
		cellgw = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l3/l5); durx1 = pulsz;   //duree locales entre debut et fin de cellule
			lx = l5;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf), \len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile1.bufnum, \start, t2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),\len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhw : craquements constants puis vers ringmod
		cellhw = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Celliw : constante transposee
		celliw = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh4/10), ((10-lh4)/10)); coeff = l2/l3;
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmin1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmx1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(-0.01*sinFunc)+(0.01*cosFunc), \len, 0.098*coeff, \dur, Pseq([0.1*coeff], durx2/(0.1*coeff)), \pan, ((-10*rampFunc*0.1*coeff)/durx2)+1+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmin4, \speed, ((-1)*l4/l6),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, -0.8,\vol, ((l4/l6)/8),\envIdx, 2); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, tmax6, \speed, (((-1)*l3)/l5),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.9,\vol, ((l6/l2)/7),\envIdx, 2); //citation
			czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmax3, \speed, (l5/l2),\len, (durx2),\dur, Pseq([durx2], 1),\pan, 0.7,\vol, ((l3/l4)/6),\envIdx, 2); //citation
			Ptpar([0.00, cxp1, durx1, cxp4, 0, czp1, ((durx1)/2), czp2, durx1, czp3], 1);
		};

		//-----Cellax : montee descente
		cellax = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh1, 2)),5); coeff = (l2/(2*l3));
			dura1 = 3*pulsz*(lha/10); dura2 = 3*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff, \dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, ((4*rampFunc*0.2*coeff)/dura1)-0.3+(0.3*sinFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff, \dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan,  ((-4*rampFunc*0.2*coeff)/dura2)+0.1+(0.3*sinFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellbx : constante mouillee montee
		cellbx = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh1; coeff = (l2/(2*l3));
			durb1 = 3*pulsz*(lhb/10); durb2 = 3*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4    ], 1);
		};

		//-----Cellcx : constante transposee
		cellcx = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh1, (1-lh1))); coeff = (l2/(2*l3));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, tmin6, \speed, (l3/l6),\len, ((durx1)/3),\dur, Pseq([((durx1)/3)], 1),\pan, -0.5,\vol, ((l2/l6)/8),\envIdx, 2); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmx5, \speed, (((-1)*l3)/l5),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.3,\vol, ((l4/l2)/7),\envIdx, 3); //citation
			czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, 0, \speed, (l5/l2),\len, ((durx1)/6),\dur, Pseq([((durx1)/6)], 1),\pan, -0.7,\vol, ((l6/l4)/6),\envIdx, 2); //citation

			Ptpar([0.00, cxp1, durx1, cxp4, czp1, ((durx1)/3), czp2, ((durx1)*(5/6)), czp3], 1);
		};

		//-----Celldx : craquements plus ou moins resonants
		celldx = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh1/10); coeff = min((l1/l5), (l5/l1));
			durx1 = pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Cellex : montee discrete inversee
		cellex = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh1/10), ((10-lh1)/10)); coeff = (l2/(2*l3));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, t3+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, t3+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp4, durx2, cxp1 ], 1);
		};

		//-----Cellfx : modulation en anneau constante, puis de craquements a ringmod
		cellfx = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 3*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t1+0.07*cosFunc, \speed, 0.8-((4*rampFunc*0.1*coeff)/durx1), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t1+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellgx
		cellgx = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l2/l3); durx1 = 3*pulsz;   //duree locales entre debut et fin de cellule
			lx = l5;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf), \len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile1.bufnum, \start, t2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf), \len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhx : craquements constants puis vers ringmod
		cellhx = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3,
			durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = pulsz*lhx; durx2 = pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmin5, \speed, ((-1*l5)/l2),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.1,\vol, ((l2/l5)/6),\envIdx, 2); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, 0, \speed, (-1*l6/l4),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, -0.1,\vol, ((l4/l6)/5),\envIdx, 1); //citation
			czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmax2, \speed, (l2/l6),\len, durx2,\dur, Pseq([durx2], 1),\pan, -0.8,\vol, ((l6/l2)/5),\envIdx, 3); //citation


			Ptpar([0.00, cxp1, durx1, cxp4, 0, czp1, ((durx1)/2), czp2, durx2, czp3], 1);
		};

		//-----Cellix : constante transposee
		cellix = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3,
			durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh4/10), ((10-lh4)/10)); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2+(0.01*sinFunc)-(0.01*cosFunc)-((8*rampFunc*0.1*coeff)/durx1), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);
			cxp2 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmin2 + (-0.09*sinFunc)+(0.10*cosFunc), \speed, 2-(0.01*sinFunc)+(0.01*cosFunc)-((8*rampFunc*0.1*coeff)/durx2), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx2/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx2)+0.8-(0.3*sinFunc), \vol, 1, \envIdx, 1);

			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmx3, \speed, (l3/l2),\len, durx1,\dur, Pseq([durx1], 1),\pan, -0.8,\vol, ((l4/l3)/5),\envIdx, 2); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmax1, \speed, (-1*l1/l4),\len, ((durx2)/2),\dur, Pseq([((durx2)/2)], 1),\pan, -0.5,\vol, ((l1/l3)/5),\envIdx, 1); //citation
			czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmin4, \speed, (l4/l5),\len, ((durx2)/2),\dur, Pseq([((durx2)/2)], 1),\pan, -0.1,\vol, ((l5/l4)/5),\envIdx, 2); //citation

			Ptpar([0.00, cxp1, durx1, cxp2, 0, czp1, durx1, czp2, ((durx1)+(durx2/2)), czp3], 1);
		};

		//-----Cellay : montee descente
		cellay = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh1, 2)),5); coeff = (l2/(2*l3));
			dura1 = 4*pulsz*(lha/10); dura2 = 3*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff,\dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff,\dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellby : constante mouillee montee
		cellby = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh1; coeff = (l2/(2*l3));
			durb1 = 5*pulsz*(lhb/10); durb2 = 5*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4    ], 1);
		};

		//-----Cellcy : constante transposee
		cellcy = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh1, (1-lh1))); coeff = l2/l3;
			durx1 = 5*pulsz*lhx; durx2 = 5*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc)-((10*rampFunc*0.20*coeff)/durx1),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celldy : craquements plus ou moins resonants
		celldy = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh1/10); coeff = min((l1/l5), (l5/l1));
			durx1 = 2*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Celley : montee discrete
		celley = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh1/10), ((10-lh1)/10)); coeff = (l2/(2*l3));
			durx1 = 5*pulsz*lhx; durx2 = 5*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, t3+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, t3+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellfy : modulation en anneau constante, puis de craquements a ringmod
		cellfy = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmx1+0.07*cosFunc, \speed, 0.8, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmx1+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmax5, \speed, (l3/l2),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.7,\vol, ((l2/l3)/6),\envIdx, 1); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, tmax6, \speed, (l3/l4),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, -0.7,\vol, ((l4/l3)/7),\envIdx, 2); //citation
			czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmin5, \speed, (((-1)*l5)/l6),\len, durx2,\dur, Pseq([durx2], 1),\pan, 0.3,\vol, ((l6/l5)/7),\envIdx, 3); //citation


			Ptpar([0.00, cxp1, durx1, cxp4, 0, czp1, ((durx1)/2), czp2, durx1, czp3 ], 1);
		};

		//-----Cellgy
		cellgy = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l3/l5); durx1 = 4*pulsz;   //duree locales entre debut et fin de cellule
			lx = l5;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2.2, 1.2, 4.2, 0.7, 2.2, 4.2, 1.2], inf), \len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile1.bufnum, \start, t2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1.2, 0.7, 2.2, 0.7, 1.2, 4.2, 2.2, 0.7, 2.2], inf), \len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhy : craquements constants puis vers ringmod
		cellhy = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmin1, \speed, (l1/l2),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.3,\vol, ((l1/l2)/5),\envIdx, 1); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmx2, \speed, (-1*l2/l4),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.7,\vol, ((l4/l2)/6),\envIdx, 1); //citation
			czp3 =  Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmax3, \speed, (l3/l6),\len, durx2,\dur, Pseq([durx2], 1),\pan, 0.9,\vol, ((l6/l3)/5),\envIdx, 2); //citation

			Ptpar([0.00, cxp1, durx1, cxp4, 0, czp1, ((durx1)/2), czp2, durx1, czp3], 1);
		};

		//-----Celliy : constante transposee
		celliy = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, czp3, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh4/10), ((10-lh4)/10)); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmax1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((-18*rampFunc*0.1*coeff)/durx1)+0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmin4, \speed, (l4/l2),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.3,\vol, ((l2/l4)/5),\envIdx, 1); //citation
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmx5, \speed, (l5/l4),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.7,\vol, ((l4/l5)/6),\envIdx, 2); //citation

			Ptpar([0.00, cxp1, 0, czp1, ((durx1)/2), czp2], 1);
		};

		//-----Cellaz : montee descente
		cellaz = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh1, 2)),5); coeff = (l2/(2*l3));
			dura1 = 4*pulsz*(lha/10); dura2 = 4*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff, \dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, ((-10*rampFunc*0.2*coeff)/dura1)+(0.3*sinFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile1.bufnum, \start, t3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff,\dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellbz : constante mouillee montee
		cellbz = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh1; coeff = (l2/(2*l3));
			durb1 = 3*pulsz*(lhb/10); durb2 = 3*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 1, \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 1+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4    ], 1);
		};

		//-----Cellcz : constante transposee
		cellcz = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh1, (1-lh1))); coeff = (l2/(2*l3));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, tmax1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, tmax1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (1.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celldz : craquements plus ou moins resonants
		celldz = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh1/10); coeff = min((l1/l5), (l5/l1));
			durx1 = 2*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.5+(0.01*sinFunc)-(0.01*cosFunc),\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Cellez : montee discrete
		cellez = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh1/10), ((10-lh1)/10)); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmin1+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile1.bufnum, \start, tmin1+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellfz : modulation en anneau constante, puis de craquements a ringmod
		cellfz = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t1+0.07*cosFunc, \speed, 0.8, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t1+0.07*cosFunc, \speed, 0.4+((4*rampFunc*0.1*coeff2)/durx2), \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellgz
		cellgz = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l3/l5); durx1 = 4*pulsz;   //duree locales entre debut et fin de cellule
			lx = l5;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, tmx1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf), \len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile1.bufnum, \start, tmx1 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf), \len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhz : craquements constants puis vers ringmod
		cellhz = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = 5*pulsz*lhx; durx2 = 5*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.5+(0.01*sinFunc)-(0.01*cosFunc)-((10*rampFunc*0.03*coeff)/durx1), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, t2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Celliz : constante transposee
		celliz = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, czp1, czp2, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh4/10), ((10-lh4)/10)); coeff = l2/l3;
			durx1 = 5*pulsz*lhx; durx2 = 5*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmax1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.2+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			// citations
			czp1 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, tmx6, \speed, (l6/l2),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, 0.4,\vol, ((l2/l6)/5),\envIdx, 1);
			czp2 =  Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, 0, \speed, (l6/l4),\len, ((durx1)/2),\dur, Pseq([((durx1)/2)], 1),\pan, -0.4,\vol, ((l4/l6)/5),\envIdx, 2); //citation


			Ptpar([0.00, cxp1, 0, czp1, (durx1/2), czp2], 1);
		};

		//-----Partitur de voix 1 du canon 4
		// Ptpar([0.00, cellbv.value], 1);

		Ptpar([czeitav, cellav.value,
			czeitbv, cellbv.value,
			czeitcv, cellcv.value,
			czeitdv, celldv.value,
			czeitev, cellev.value,
			czeitfv, cellfv.value,
			czeitgv, cellgv.value,
			czeithv, cellhv.value,
			czeitiv, celliv.value,
			czeitaw, cellaw.value,
			czeitbw, cellbw.value,
			czeitcw, cellcw.value,
			czeitdw, celldw.value,
			czeitew, cellew.value,
			czeitfw, cellfw.value,
			czeitgw, cellgw.value,
			czeithw, cellhw.value,
			czeitiw, celliw.value,
			czeitax, cellax.value,
			czeitbx, cellbx.value,
			czeitcx, cellcx.value,
			czeitdx, celldx.value,
			czeitex, cellex.value,
			czeitfx, cellfx.value,
			czeitgx, cellgx.value,
			czeithx, cellhx.value,
			czeitix, cellix.value,
			czeitay, cellay.value,
			czeitby, cellby.value,
			czeitcy, cellcy.value,
			czeitdy, celldy.value,
			czeitey, celley.value,
			czeitfy, cellfy.value,
			czeitgy, cellgy.value,
			czeithy, cellhy.value,
			czeitiy, celliy.value,
			czeitaz, cellaz.value,
			czeitbz, cellbz.value,
			czeitcz, cellcz.value,
			czeitdz, celldz.value,
			czeitez, cellez.value,
			czeitfz, cellfz.value,
			czeitgz, cellgz.value,
			czeithz, cellhz.value,
			czeitiz, celliz.value
			], 1);
	};

	// -----------------------------------
	//----------voix 2 du canon 4 ---------
	// -----------------------------------

	voix2= {

		var
		czeitav,
		celleu2, cellfu2, cellgu2, cellhu2, celliu2,
		czeiteu2, czeitfu2, czeitgu2, czeithu2, czeitiu2,
		cellav2, cellbv2, cellcv2, celldv2, cellev2, cellfv2, cellgv2, cellhv2, celliv2,       // voixII
		czeitav2, czeitbv2, czeitcv2, czeitdv2, czeitev2, czeitfv2, czeitgv2, czeithv2, czeitiv2,
		cellaw2, cellbw2, cellcw2, celldw2, cellew2, cellfw2, cellgw2, cellhw2, celliw2,
		czeitaw2, czeitbw2, czeitcw2, czeitdw2, czeitew2, czeitfw2, czeitgw2, czeithw2, czeitiw2,
		cellax2, cellbx2, cellcx2, celldx2, cellex2, cellfx2, cellgx2, cellhx2, cellix2,
		czeitax2, czeitbx2, czeitcx2, czeitdx2, czeitex2, czeitfx2, czeitgx2, czeithx2, czeitix2,
		cellay2, cellby2, cellcy2, celldy2, celley2, cellfy2, cellgy2, cellhy2, celliy2,
		czeitay2, czeitby2, czeitcy2, czeitdy2, czeitey2, czeitfy2, czeitgy2, czeithy2, czeitiy2,
		cellaz2, cellbz2, cellcz2, celldz2,
		czeitaz2, czeitbz2, czeitcz2, czeitdz2
		;

		//------partition du temps
		czeitav = 0;
		czeiteu2 = czeitav + (2*pulsz);   // voix II
		czeitfu2 = czeitav + (6*pulsz);
		czeitgu2 = czeitav + (8*pulsz);
		czeithu2 = czeitav + (11*pulsz);
		czeitiu2 = czeitav + (15*pulsz);
		czeitav2 = czeitav + (16*pulsz);
		czeitbv2 = czeitav + (20*pulsz);
		czeitcv2 = czeitav + (23*pulsz);
		czeitdv2 = czeitav + (25*pulsz);
		czeitev2 = czeitav + (29*pulsz);
		czeitfv2 = czeitav + (33*pulsz);
		czeitgv2 = czeitav + (35*pulsz);
		czeithv2 = czeitav + (38*pulsz);
		czeitiv2 = czeitav + (42*pulsz);
		czeitaw2 = czeitav + (43*pulsz);
		czeitbw2 = czeitav + (47*pulsz);
		czeitcw2 = czeitav + (50*pulsz);
		czeitdw2 = czeitav + (52*pulsz);
		czeitew2 = czeitav + (56*pulsz);
		czeitfw2 = czeitav + (60*pulsz);
		czeitgw2 = czeitav + (62*pulsz);
		czeithw2 = czeitav + (65*pulsz);
		czeitiw2 = czeitav + (69*pulsz);
		czeitax2 = czeitav + (70*pulsz);
		czeitbx2 = czeitav + (74*pulsz);
		czeitcx2 = czeitav + (77*pulsz);
		czeitdx2 = czeitav + (79*pulsz);
		czeitex2 = czeitav + (83*pulsz);
		czeitfx2 = czeitav + (87*pulsz);
		czeitgx2 = czeitav + (89*pulsz);
		czeithx2 = czeitav + (92*pulsz);
		czeitix2 = czeitav + (96*pulsz);
		czeitay2 = czeitav + (97*pulsz);
		czeitby2 = czeitav + (101*pulsz);
		czeitcy2 = czeitav + (104*pulsz);
		czeitdy2 = czeitav + (106*pulsz);
		czeitey2 = czeitav + (110*pulsz);
		czeitfy2 = czeitav + (114*pulsz);
		czeitgy2 = czeitav + (116*pulsz);
		czeithy2 = czeitav + (119*pulsz);
		czeitiy2 = czeitav + (123*pulsz);
		czeitaz2 = czeitav + (124*pulsz);
		czeitbz2 = czeitav + (128*pulsz);
		czeitcz2 = czeitav + (131*pulsz);
		czeitdz2 = czeitav + (133*pulsz);

		//-------------------- Voix II ---------------------
		//-----Celleu2 : montee discrete
		celleu2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh2/10), ((10-lh2)/10)); coeff = l2/l3;
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmin2+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmin2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmin2-0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.2*coeff)/durx2),\freq, fmin2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellfu2 : modulation en anneau constante, puis de craquements a ringmod
		cellfu2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 1.5*pulsz*lhx; durx2 = 1.5*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmax2+0.07*cosFunc, \speed, 1.2, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmax2+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellgu2
		cellgu2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l3/l5); durx1 = pulsz;   //duree locales entre debut et fin de cellule
			lx = l2;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),\len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhu2 : craquements constants puis vers ringmod
		cellhu2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmin2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmin2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (-1+((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Celliu2 : constante transposee
		celliu2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh2/10), ((10-lh2)/10)); coeff = l2/l3;
			durx1 = 1.1*pulsz*lhx; durx2 = 1.1*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1  ], 1);
		};

		//-----Cellav2 : montee descente
		cellav2 = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh2, 2)),5); coeff = l2/l3;
			dura1 = pulsz*(lha/10); dura2 = pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, tmax2 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.1*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.1*coeff/dura1)*0.5*rampFunc))*coeff,
				\dur, Pseq([0.1*coeff], dura1/(0.1*coeff)), \pan, ((-12*rampFunc*0.1*coeff)/dura1)+1+(0.3*sinFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, tmax2 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.1*coeff/dura2)*5*rampFunc)), \len, (0.1-((0.1*coeff/dura1)*0.5*rampFunc))*coeff,
				\dur, Pseq([0.1*coeff], dura2/(0.1*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.1*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellbv2 : constante mouillee montee
		cellbv2 = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh2; coeff = l2/l3;
			durb1 = 3*pulsz*(lhb/10); durb2 = 3*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmin2 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2-((10*rampFunc*0.10*coeff)/durb1), \len, 0.10*coeff,\dur, Pseq([0.10*coeff], durb1/(0.10*coeff)), \pan, (-1+((14*rampFunc*0.10*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmin2 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 1+((10*rampFunc*0.10*coeff)/durb2), \len, 0.10*coeff,\dur, Pseq([0.10*coeff], durb2/(0.10*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.10*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.10*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4   ], 1);
		};

		//-----Cellcv2 : constante transposee
		cellcv2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh2, (1-lh2))); coeff = l2/l3;
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmin2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.10*coeff, \dur, Pseq([0.10*coeff], durx1/(0.10*coeff)), \pan, (0.8-((10*rampFunc*0.10*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmin2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.10*coeff, \dur, Pseq([0.10*coeff], durx2/(0.10*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celldv2 : craquements plus ou moins resonants
		celldv2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh2/10); coeff = min((l2/l5), (l5/l2));
			durx1 = 2*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Cellev2 : montee discrete
		cellev2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh2/10), ((10-lh2)/10)); coeff = l2/l3;
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmx2+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.1*coeff)/durx1), \len, 0.095*coeff,\freq, fmax2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.1*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmx2+0.9*cosFunc, \speed, 3.3, \len, 0.095*coeff-((0.5*rampFunc*0.095*coeff)/durx2),\freq, fmax2, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.1*coeff], durx2/(0.1*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.1*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellfv2 : modulation en anneau constante, puis de craquements a ringmod inverse ici
		cellfv2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 2*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u1+0.07*cosFunc, \speed, 0.8, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u1+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp4, durx2, cxp1 ], 1);
		};

		//-----Cellgv2
		cellgv2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l3/l5); durx1 = pulsz;   //duree locales entre debut et fin de cellule
			lx = l2;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),
				\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile2.bufnum, \start, u2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),\len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhv2 : craquements constants puis vers ringmod
		cellhv2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmin2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmin2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Celliv2 : constante transposee
		celliv2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh2/10), ((10-lh2)/10)); coeff = (l2/l3);
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmax2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((-20*rampFunc*0.1*coeff)/durx1)+1+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1  ], 1);
		};

		//-----Cellaw2 : montee descente
		cellaw2 = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh2, 2)),5); coeff = (l2/(2*l3));
			dura1 = 3*pulsz*(lha/10); dura2 =3* pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, tmax2 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff,
				\dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, ((-10*rampFunc*0.2*coeff)/dura1)+0.3+(0.3*sinFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, tmax2 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff,
				\dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, ((15*rampFunc*0.2*coeff)/dura2)-0.7+(0.3*sinFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellbw2 : constante mouillee montee
		cellbw2 = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh2; coeff = (l2/(2*l3));
			durb1 = 3.5*pulsz*(lhb/10); durb2 = 3.5*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u3 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.20*coeff,
				\dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u3 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff,\dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4   ], 1);
		};

		//-----Cellcw2 : constante transposee
		cellcw2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh2, (1-lh2))); coeff = (l2/(2*l3));
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc)+((10*rampFunc*0.20*coeff)/durx1),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celldw2 : craquements plus ou moins resonants
		celldw2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh2/10); coeff = min((l2/l5), (l5/l2));
			durx1 = pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Cellew2 : montee discrete
		cellew2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh2/10), ((10-lh2)/10)); coeff = l2/l3;
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, u3+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, u3+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellfw2 : modulation en anneau constante, puis de craquements a ringmod
		cellfw2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 3*pulsz*lhx; durx2 = pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u1+0.07*cosFunc, \speed, 0.8-((4*rampFunc*0.1*coeff)/durx1), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u1+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellgw2
		cellgw2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l2/l3); durx1 = 2*pulsz;   //duree locales entre debut et fin de cellule
			lx = l2;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),
				\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile2.bufnum, \start, u2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),\len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhw2 : craquements constants puis vers ringmod
		cellhw2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Celliw2 : constante transposee
		celliw2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh2/10), ((10-lh2)/10)); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u3 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc)- ((18*rampFunc*0.1*coeff)/durx1),
				\len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1  ], 1);
		};

		//-----Cellax2 : montee descente
		cellax2 = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh2, 2)),5); coeff = (l2/(2*l3));
			dura1 = 3.5*pulsz*(lha/10); dura2 = 3.5*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, u3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff,\dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, u3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff,\dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellbx2 : constante mouillee montee
		cellbx2 = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh2; coeff = l2/l3;
			durb1 = 5*pulsz*(lhb/10); durb2 = 5*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmin2 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2-((10*rampFunc*0.20*coeff)/durb1), \len, 0.20*coeff,\dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmin2 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 1+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff,\dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4   ], 1);
		};

		//-----Cellcx2 : constante transposee
		cellcx2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh2, (1-lh2))); coeff = (l2/(2*l3));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (1.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celldx2 : craquements plus ou moins resonants
		celldx2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh2/10); coeff = min((l2/l5), (l5/l2));
			durx1 = pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc)-((16*rampFunc*0.03*coeff)/durx1),\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Cellex2 : montee discrete
		cellex2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh2/10), ((10-lh2)/10)); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmax2+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmax2+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellfx2 : modulation en anneau constante, puis de craquements a ringmod inversee
		cellfx2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u3+(0.07*cosFunc), \speed, 0.8, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u3 +(0.07*cosFunc), \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp4, durx2, cxp1 ], 1);
		};

		//-----Cellgx2
		cellgx2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l3/l5); durx1 = 5*pulsz;   //duree locales entre debut et fin de cellule
			lx = l2;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),
				\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile2.bufnum, \start, u1 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),\len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhx2 : craquements constants puis vers ringmod
		cellhx2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = 2*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Cellix2 : constante transposee
		cellix2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh2/10), ((10-lh2)/10)); coeff = l2/l3;
			durx1 = 5*pulsz*lhx; durx2 = 5*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmx2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1  ], 1);
		};

		//-----Cellay2 : montee descente
		cellay2 = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh2, 2)),5); coeff = (l2/(2*l3));
			dura1 = 5*pulsz*(lha/10); dura2 = 5*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff,\dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff,\dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellby2 : constante mouillee montee
		cellby2 = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh2; coeff = (l2/(2*l3));
			durb1 = 4*pulsz*(lhb/10); durb2 = 4*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmax2 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 1.3, \len, 0.20*coeff,
				\dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmax2 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 1.3+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff,\dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4   ], 1);
		};

		//-----Cellcy2 : constante transposee
		cellcy2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh2, (1-lh2))); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (1.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celldy2 : craquements plus ou moins resonants
		celldy2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh2/10); coeff = min((l2/l5), (l5/l2));
			durx1 = 2*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Celley2 : montee discrete
		celley2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh2/10), ((10-lh2)/10)); coeff = (l2/(2*l3));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmx2+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile2.bufnum, \start, tmx2+0.9*cosFunc, \speed, 3.3-((25*rampFunc*0.2*coeff)/durx2), \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellfy2 : modulation en anneau constante, puis de craquements a ringmod inversee
		cellfy2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = l2/l3; coeff2 = max((min ((l2/l5), (l5/l2))), 0.5);
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmax2+0.07*cosFunc, \speed, 0.8, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmax2+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp4, durx2, cxp1 ], 1);
		};

		//-----Cellgy2
		cellgy2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l3/l5); durx1 = 4*pulsz;   //duree locales entre debut et fin de cellule
			lx = l2;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2.1, 1.1, 4.1, 0.6, 2.1, 4.1, 1.1], inf),\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1.1, 0.6, 2.1, 0.6, 1.1, 4.1, 2.1, 0.6, 2.1], inf),\len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellhy2 : craquements constants puis vers ringmod
		cellhy2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh2/10); coeff = max((min ((l2/(2*l5)), (l5/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l5), (((2*l5)/l2)))), 1.5);
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, u2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Celliy2 : constante transposee
		celliy2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh2/10), ((10-lh2)/10)); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmax2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.5+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1  ], 1);
		};

		//-----Cellaz2 : montee descente
		cellaz2 = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh2, 2)),5); coeff = (l2/(2*l3));
			dura1 = 4*pulsz*(lha/10); dura2 = 4*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff,\dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff,\dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellbz2 : constante mouillee montee
		cellbz2 = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh2; coeff = (l2/(2*l3));
			durb1 = 4*pulsz*(lhb/10); durb2 = 4*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 1, \len, 0.20*coeff,\dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, u1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 1+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff,\dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4   ], 1);
		};

		//-----Cellcz2 : constante transposee
		cellcz2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh2, (1-lh2))); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmax2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmax2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (2.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celldz2 : craquements plus ou moins resonants
		celldz2 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh2/10); coeff = min((l2/l5), (l5/l2));
			durx1 = 11*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile2.bufnum, \start, tmx2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Partitur de voix 2 du canon 4
		// Ptpar([0.00, cellbv2.value], 1);

		Ptpar([
			czeiteu2, celleu2.value,
			czeitfu2, cellfu2.value,
			czeitgu2, cellgu2.value,
			czeithu2, cellhu2.value,
			czeitiu2, celliu2.value,
			czeitav2, cellav2.value,
			czeitbv2, cellbv2.value,
			czeitcv2, cellcv2.value,
			czeitdv2, celldv2.value,
			czeitev2, cellev2.value,
			czeitfv2, cellfv2.value,
			czeitgv2, cellgv2.value,
			czeithv2, cellhv2.value,
			czeitiv2, celliv2.value,
			czeitaw2, cellaw2.value,
			czeitbw2, cellbw2.value,
			czeitcw2, cellcw2.value,
			czeitdw2, celldw2.value,
			czeitew2, cellew2.value,
			czeitfw2, cellfw2.value,
			czeitgw2, cellgw2.value,
			czeithw2, cellhw2.value,
			czeitiw2, celliw2.value,
			czeitax2, cellax2.value,
			czeitbx2, cellbx2.value,
			czeitcx2, cellcx2.value,
			czeitdx2, celldx2.value,
			czeitex2, cellex2.value,
			czeitfx2, cellfx2.value,
			czeitgx2, cellgx2.value,
			czeithx2, cellhx2.value,
			czeitix2, cellix2.value,
			czeitay2, cellay2.value,
			czeitby2, cellby2.value,
			czeitcy2, cellcy2.value,
			czeitdy2, celldy2.value,
			czeitey2, celley2.value,
			czeitfy2, cellfy2.value,
			czeitgy2, cellgy2.value,
			czeithy2, cellhy2.value,
			czeitiy2, celliy2.value,
			czeitaz2, cellaz2.value,
			czeitbz2, cellbz2.value,
			czeitcz2, cellcz2.value,
			czeitdz2, celldz2.value
			], 1);
	};

	// -----------------------------------
	//----------voix 3 du canon 4 ---------
	// -----------------------------------

	voix3= {
		var  czeitav,
		cella3, cellb3, cellc3, celld3, celle3, cellf3, cellg3, cellh3, celli3,      // voixIII
		czeita3, czeitb3, czeitc3, czeitd3, czeite3, czeitf3, czeitg3, czeith3, czeiti3
		;

		//------partition du temps
		czeitav = 0;
		czeiti3 = czeitav + (5*pulsz); // voix III
		czeita3 = czeitav + (10*pulsz);
		czeitb3 = czeitav + (30*pulsz);
		czeitc3 = czeitav + (45*pulsz);
		czeitd3 = czeitav + (55*pulsz);
		czeite3 = czeitav + (75*pulsz);
		czeitf3 = czeitav + (95*pulsz);
		czeitg3 = czeitav + (105*pulsz);
		czeith3 = czeitav + (120*pulsz);

		//-------------------- Voix III ---------------------
		//-----Celli3 : constante transposee
		celli3 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh3/10), ((10-lh3)/10)); coeff = l2/l3;
			durx1 = 1.4*pulsz*lhx; durx2 = 1.4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, v2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((-18*rampFunc*0.1*coeff)/durx1)+0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1  ], 1);
		};

		//-----Cella3 : montee descente
		cella3 = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh3, 2)),5); coeff = l2/l3;
			dura1 = pulsz*(lha/10); dura2 = pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile3.bufnum, \start, tmax3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.1*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.1*coeff/dura1)*0.5*rampFunc))*coeff,\dur, Pseq([0.1*coeff], dura1/(0.1*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile3.bufnum, \start, tmax3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.1*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.1*coeff/dura1)*0.5*rampFunc))*coeff, \dur, Pseq([0.1*coeff], dura2/(0.1*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.1*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellb3 : constante mouillee montee
		cellb3 = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh3; coeff = l2/l3;
			durb1 = 3*pulsz*(lhb/10); durb2 = 3*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile3.bufnum, \start, tmx3 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.10*coeff, \dur, Pseq([0.10*coeff], durb1/(0.10*coeff)), \pan, (-1+((14*rampFunc*0.10*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile3.bufnum, \start, tmx3 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.10*coeff)/durb2), \len, 0.10*coeff, \dur, Pseq([0.10*coeff], durb2/(0.10*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.10*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.10*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4    ], 1);
		};

		//-----Cellc3 : constante transposee
		cellc3 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh3, (1-lh3))); coeff = l2/l3;
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile3.bufnum, \start, v1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile3.bufnum, \start, v2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celld3 : craquements plus ou moins resonants
		celld3 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh3/10); coeff = min((l2/l5), (l5/l2));
			durx1 = 2*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile3.bufnum, \start, v2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Celle3 : montee discrete inversee
		celle3 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh3/10), ((10-lh3)/10)); coeff = (l2/(2*l3));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, v3+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile3.bufnum, \start, v3+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp4, durx2, cxp1 ], 1);
		};

		//-----Cellf3 : modulation en anneau constante, puis de craquements a ringmod
		cellf3 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh3/10); coeff = l2/l3; coeff2 = max((min ((l3/l5), (l5/l3))), 0.5);
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, tmax3+0.07*cosFunc, \speed, 0.8+((5*rampFunc*0.1*coeff)/durx1), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, tmax3+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellg3
		cellg3 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l2/l3); durx1 = 4*pulsz;   //duree locales entre debut et fin de cellule
			lx = l3;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile3.bufnum, \start, tmax3 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf), \len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile3.bufnum, \start, tmax3 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf), \len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellh3 : craquements constants puis vers ringmod
		cellh3 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh3/10); coeff = max((min ((l2/(2*l3)), (l3/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l3), (((2*l3)/l2)))), 1.5);
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile3.bufnum, \start, tmax3 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, tmax3 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Partitur de voix 3 du canon 4
		// Ptpar([0.00, cella3.value], 1);

		Ptpar([
			czeita3, cella3.value,
			czeitb3, cellb3.value,
			czeitc3, cellc3.value,
			czeitd3, celld3.value,
			czeite3, celle3.value,
			czeitf3, cellf3.value,
			czeitg3, cellg3.value,
			czeith3, cellh3.value,
			czeiti3, celli3.value
			], 1);
	};

	// -----------------------------------
	//----------voix 4 du canon 4 ---------
	// -----------------------------------

	voix4 = {
		var czeitav,
		cella4, cellb4, cellc4, celld4, celle4, cellf4, cellg4, cellh4, celli4,       // voix 4
		czeita4, czeitb4, czeitc4, czeitd4, czeite4, czeitf4, czeitg4, czeith4, czeiti4
		;

		//------partition du temps
		czeitav = 0;
		czeith4 = czeitav + (12*pulsz); // voix IV
		czeiti4 = czeitav + (32*pulsz);
		czeita4 = czeitav + (37*pulsz);
		czeitb4 = czeitav + (57*pulsz);
		czeitc4 = czeitav + (72*pulsz);
		czeitd4 = czeitav + (82*pulsz);
		czeite4 = czeitav + (102*pulsz);
		czeitf4 = czeitav + (122*pulsz);
		czeitg4 = czeitav + (132*pulsz);

		//-----Cellh4 : craquements constants puis vers ringmod
		cellh4 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh4/10); coeff = max((min ((l2/(2*l3)), (l3/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l3), (((2*l3)/l2)))), 1.5);
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile4.bufnum, \start, w2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, w2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Celli4 : constante transposee
		celli4 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh4/10), ((10-lh4)/10)); coeff = l2/l3;
			durx1 = 2.1*pulsz*lhx; durx2 = 2.1*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, w2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 2.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1  ], 1);
		};

		//-----Cella4 : montee descente
		cella4 = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh4, 2)),5); coeff = (l2/(2*l3));
			dura1 = 2*pulsz*(lha/10); dura2 = 2*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile4.bufnum, \start, w3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff,
				\dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, (-0.7*sinFunc)+(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile4.bufnum, \start, w3 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff,
				\dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (-0.7*sinFunc)+(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellb4 : constante mouillee montee
		cellb4 = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh4; coeff = l2/l3;
			durb1 = 3*pulsz*(lhb/10); durb2 = 3*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile4.bufnum, \start, w1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.20*coeff,
				\dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile4.bufnum, \start, w1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff,\dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4   ], 1);
		};

		//-----Cellc4 : constante transposee
		cellc4 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh4, (1-lh4))); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile4.bufnum, \start, w1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile4.bufnum, \start, w2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celld4 : craquements plus ou moins resonants
		celld4 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh4/10); coeff = min((l2/l4), (l5/l4));
			durx1 = 3*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile4.bufnum, \start, w2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc)-((6*rampFunc*0.03*coeff)/durx1),\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.3-((6*rampFunc*0.03*coeff)/durx1)+compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Celle4 : montee discrete
		celle4 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh4/10), ((10-lh4)/10)); coeff = (l2/(2*l3));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, tmax4+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile4.bufnum, \start, tmax4+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellf4 : modulation en anneau constante, puis de craquements a ringmod inversee
		cellf4 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh4/10); coeff = l2/l3; coeff2 = max((min ((l3/l4), (l4/l3))), 0.5);
			durx1 = 3*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, tmx4+0.07*cosFunc, \speed, 0.8, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, tmx4+0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp4, durx2, cxp1 ], 1);
		};

		//-----Cellg4
		cellg4 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l2/l3); durx1 = 12*pulsz;   //duree locales entre debut et fin de cellule
			lx = l4;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile4.bufnum, \start, tmax4 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile4.bufnum, \start, tmax4 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf),\len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Partitur de voix 4 du canon 4
		//  Ptpar([0.00, cella4.value], 1);

		Ptpar([
			czeita4, cella4.value,
			czeitb4, cellb4.value,
			czeitc4, cellc4.value,
			czeitd4, celld4.value,
			czeite4, celle4.value,
			czeitf4, cellf4.value,
			czeitg4, cellg4.value,
			czeith4, cellh4.value,
			czeiti4, celli4.value
			], 1);
	};

	// -----------------------------------
	//----------voix 5 du canon 4 ---------
	// -----------------------------------

	voix5= {
		var czeitav,
		cella5, cellb5, cellc5, celld5, celle5, cellf5, cellg5, cellh5, celli5,      // voix 5
		czeita5, czeitb5, czeitc5, czeitd5, czeite5, czeitf5, czeitg5, czeith5, czeiti5
		;

		//------partition du temps
		czeitav = 0;
		czeita5 = czeitav + (14*pulsz); // voix V
		czeitb5 = czeitav + (24*pulsz);
		czeitc5 = czeitav + (39*pulsz);
		czeitd5 = czeitav + (59*pulsz);
		czeite5 = czeitav + (64*pulsz);
		czeitf5 = czeitav + (84*pulsz);
		czeitg5 = czeitav + (99*pulsz);
		czeith5 = czeitav + (109*pulsz);
		czeiti5 = czeitav + (129*pulsz);

		//-----Cella5 : montee descente
		cella5 = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh5, 2)),5); coeff = l2/l3;
			dura1 = 1.1*pulsz*(lha/10); dura2 = 1.1*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile5.bufnum, \start, tmx5 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff, \dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile5.bufnum, \start, tmx5 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff, \dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellb5 : constante mouillee montee
		cellb5 = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh5; coeff = l2/l3;
			durb1 = 3*pulsz*(lhb/10); durb2 = 3*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, y1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.10*coeff,\dur, Pseq([0.10*coeff], durb1/(0.10*coeff)), \pan, (-1+((14*rampFunc*0.10*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, y1 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.10*coeff)/durb2), \len, 0.10*coeff,\dur, Pseq([0.10*coeff], durb2/(0.10*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.10*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.10*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4    ], 1);
		};

		//-----Cellc5 : constante transposee
		cellc5 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh5, (1-lh5))); coeff = (l2/(2*l3));
			durx1 = pulsz*lhx; durx2 = pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, y1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, y2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Celld5 : craquements plus ou moins resonants
		celld5 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh5/10); coeff = min((l2/l5), (l5/l5));
			durx1 = 2*pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, y2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (-1+((6*rampFunc*0.03*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Celle5 : montee discrete
		celle5 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh5/10), ((10-lh5)/10)); coeff = l2/l3;
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, tmx5+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile5.bufnum, \start, tmx5+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellf5 : modulation en anneau constante, puis de craquements a ringmod
		cellf5 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh5/10); coeff = l2/l3; coeff2 = max((min ((l3/l5), (l5/l3))), 0.5);
			durx1 = pulsz*lhx; durx2 = pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, y1+0.07*cosFunc, \speed, 0.8, \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, y1+0.07*cosFunc, \speed, 0.4+((4*rampFunc*0.1*coeff2)/durx2), \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellg5
		cellg5 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l2/l3); durx1 = 5*pulsz;   //duree locales entre debut et fin de cellule
			lx = l5;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, tmax5 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf), \len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile5.bufnum, \start, tmx5 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf), \len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellh5 : craquements constants puis vers ringmod
		cellh5 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh5/10); coeff = max((min ((l2/(2*l3)), (l3/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l3), (((2*l3)/l2)))), 1.5);
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile5.bufnum, \start, tmx5 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, tmx5 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc), \len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Celli5 : constante transposee
		celli5 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh5/10), ((10-lh5)/10)); coeff = l2/l3;
			durx1 = 6*pulsz*lhx; durx2 = 6*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, tmax5 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.7+(0.01*sinFunc)-(0.01*cosFunc)-((10*rampFunc*0.1*coeff)/durx1), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1  ], 1);
		};

		//-----Partitur de voix 5 du canon 4
		// Ptpar([0.00, cella5.value], 1);

		Ptpar([
			czeita5, cella5.value,
			czeitb5, cellb5.value,
			czeitc5, cellc5.value,
			czeitd5, celld5.value,
			czeite5, celle5.value,
			czeitf5, cellf5.value,
			czeitg5, cellg5.value,
			czeith5, cellh5.value,
			czeiti5, celli5.value
			], 1);
	};

	// -----------------------------------
	//----------voix 6 du canon 4 ---------
	// -----------------------------------
	voix6= {
		var czeitav,
		cella6, cellb6, cellc6, celld6, celle6, cellf6, cellg6, cellh6, celli6,      // voix 6
		czeita6, czeitb6, czeitc6, czeitd6, czeite6, czeitf6, czeitg6, czeith6, czeiti6
		;

		//------partition du temps
		czeitav = 0;
		czeitd6 = czeitav + (1*pulsz); // voix VI
		czeite6 = czeitav + (21*pulsz);
		czeitf6 = czeitav + (41*pulsz);
		czeitg6 = czeitav + (51*pulsz);
		czeith6 = czeitav + (66*pulsz);
		czeiti6 = czeitav + (86*pulsz);
		czeita6 = czeitav + (91*pulsz);
		czeitb6 = czeitav + (111*pulsz);
		czeitc6 = czeitav + (126*pulsz);

		//-------------------- Voix VI ---------------------

		//-----Celld6 : craquements plus ou moins resonants
		celld6 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = (lh6/10); coeff = min((l2/l6), (l5/l6));
			durx1 = pulsz;

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (-0.5+((6*rampFunc*0.03*coeff)/durx1)-compFunc1), \vol, 0.4+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1 ], 1);
		};

		//-----Celle6 : montee discrete
		celle6 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			lhx = max((lh6/10), ((10-lh6)/10)); coeff = l2/l3;
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1= Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, tmx6+0.9*cosFunc, \speed, 0.8+((25*rampFunc*0.2*coeff)/durx1), \len, 0.195*coeff,\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, (-1)*(cos((0.3)*exp(rampFunc))), \vol, 0.7+((3*rampFunc*0.2*coeff)/durx1), \envIdx, 5);
			cxp4= Pbind(\instrument, \instFiltr,\bufNum, soundFile6.bufnum, \start, tmx6+0.9*cosFunc, \speed, 3.3, \len, 0.195*coeff-((1.5*rampFunc*0.195*coeff)/durx2),\freq, fmax5, \q, 1.1,\frqEnvIdx, 1, \dur,  Pseq([0.2*coeff], durx2/(0.2*coeff)), \pan, (0.8*sinFunc)-(0.6*cosFunc), \vol, 1-((6*rampFunc*0.2*coeff)/durx2), \envIdx, 5);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellf6 : modulation en anneau constante, puis de craquements a ringmod
		cellf6 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = l2/l3; coeff2 = max((min ((l3/l6), (l6/l3))), 0.5);
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, tmax6 + 0.07*cosFunc, \speed, 0.8-((4*rampFunc*0.1*coeff)/durx1), \len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1-((10*rampFunc*0.1*coeff)/durx1), \envIdx, 3);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, tmax6 + 0.07*cosFunc, \speed, 0.4, \len, 0.095*(coeff2+((coeff-coeff2)*((10*rampFunc*0.1*coeff2)/durx2))), \dur, Pseq([0.1*coeff2], durx2/(0.1*coeff2)), \pan, ((10*rampFunc*0.1*coeff2)/durx2)*(0.7*(cosFunc+sinFunc)), \vol, 1, \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Cellg6
		cellg6 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, lx, coeff, r1, r2, r3, r4, r5, rm;
			coeff = (l2/l3); durx1 = 3*pulsz;   //duree locales entre debut et fin de cellule
			lx = l6;
			r1 = floor(10*((lx-(floor(lx)))))/100; r2 = floor(10*(((10*lx)-(floor(10*lx)))))/100; r3 = floor(10*(((100*lx)-(floor(100*lx)))))/100; r4 = max((floor(10*(((1000*lx)-(floor(1000*lx)))))/100), 0.01); r5 = floor(10*(((lx/10)-(floor(lx/10)))))/100; // differentes decimales de l5
			rm = (r1 + r2 + r3 + r4 + r5); // attention pour avoir une duree de pulsz  ne pas diviser par 5

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z2 + (0.09*sinFunc)-(0.10*cosFunc), \speed, Pseq(#[2, 1, 4, 0.5, 2, 4, 1], inf),\len, Pseq([r1, r2, r3, r4, r5], inf), \dur, Pseq([r1, r2, r3, r4, r5], durx1/rm), \pan, (0.8-((4*rampFunc*rm)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 5);
			cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(1000*squared(0.7*sinFunc))-(400*cosFunc), \filtQ, 0.7, \bufNum, soundFile6.bufnum, \start, z2 + (0.12*sinFunc)+(0.05*cosFunc), \speed, Pseq(#[1, 0.5, 2, 0.5, 1, 4, 2, 0.5, 2], inf), \len, Pseq([r5, r4, r2, r3, r1], inf), \dur, Pseq([r5, r4, r2, r3, r1], durx1/rm),\pan,  (0.8-compFunc1), \vol, 0.6+(0.2*sinFunc)+(0.2*cosFunc), \envIdx, 5);

			Ptpar([0.00, cxp1, 0.00, cxp3 ], 1);
		};

		//-----Cellh6 : craquements constants puis vers ringmod
		cellh6 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;
			lhx = (lh6/10); coeff = max((min ((l2/(2*l3)), (l3/(2*l2)))), 0.5); coeff2 = min((max (((2*l2)/l3), (((2*l3)/l2)))), 1.5);
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, z1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.2+(0.01*sinFunc)-(0.01*cosFunc),\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (0.8-((16*rampFunc*0.03*coeff)/durx1)), \vol, 0.5+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, z1 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.2+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.075*(coeff+((coeff2-coeff)*((10*rampFunc*0.03*coeff2)/durx2))), \dur, Pseq([0.03*coeff2], durx2/(0.03*coeff2)), \pan, (1-((10*rampFunc*0.03*coeff2)/durx2))*(-0.8*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4], 1);
		};

		//-----Celli6 : constante transposee
		celli6 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff, coeff2;

			lhx = min((lh6/10), ((10-lh6)/10)); coeff = l2/l3;
			durx1 = 5*pulsz*lhx; durx2 = 5*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, tmax6 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 1.5+(0.01*sinFunc)-(0.01*cosFunc)- ((5*rampFunc*0.1*coeff)/durx1),
				\len, 0.095*coeff, \dur, Pseq([0.1*coeff], durx1/(0.1*coeff)), \pan, ((18*rampFunc*0.1*coeff)/durx1)-0.8+(0.3*sinFunc), \vol, 1, \envIdx, 1);

			Ptpar([0.00, cxp1  ], 1);
		};

		//-----Cella6 : montee descente
		cella6 = {
			var cap1, cap2, cap3, cap4, cap5, cap6, dura1, dura2, lha, coeff;
			lha = min((max(lh6, 2)),5); coeff = (l2/(2*l3));
			dura1 = 5*pulsz*(lha/10); dura2 = 5*pulsz*((10-lha)/10);  //duree locales entre debut et fin de cellule

			cap1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile6.bufnum, \start, tmx6 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (-3+((0.2*coeff/dura1)*55*rampFunc)), \len, (0.05+((0.2*coeff/dura1)*1*rampFunc))*coeff,
				\dur, Pseq([0.2*coeff], dura1/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, ((0.5*sinFunc)+(0.7*cosFunc)), \envIdx, 1);
			cap4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[100, 700, 1000, 1400, 1900], inf), \filtQ, 0.9, \bufNum, soundFile6.bufnum, \start, tmx6 + (0.12*sinFunc)-(0.15*cosFunc), \speed, (2.5-((0.2*coeff/dura2)*5*rampFunc)), \len, (0.15-((0.2*coeff/dura1)*1*rampFunc))*coeff,
				\dur, Pseq([0.2*coeff], dura2/(0.2*coeff)), \pan, (0.7*sinFunc)-(0.8*cosFunc), \vol, (((0.5*sinFunc)+(0.7*cosFunc))*(1.1-((0.2*coeff/dura2)*12*rampFunc))), \envIdx, 1);

			Ptpar([0.00, cap1, dura1, cap4], 1);
		};

		//-----Cellb6 : constante mouillee montee
		cellb6 = {
			var cbp1, cbp2, cbp3, cbp4, cbp5, cbp6, durb1, durb2, lhb, coeff;
			lhb = lh6; coeff = (l2/(2*l3));
			durb1 = 4*pulsz*(lhb/10); durb2 = 4*pulsz*((10-lhb)/10);  //duree locales entre debut et fin de cellule

			cbp1 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, tmax6 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2, \len, 0.20*coeff,
				\dur, Pseq([0.20*coeff], durb1/(0.20*coeff)), \pan, (-1+((14*rampFunc*0.20*coeff)/durb1)), \vol, (-0.7*sinFunc)+(0.5*cosFunc), \envIdx, 1);
			cbp4 = Pbind(\instrument, \instRLPF, \filtFrq, Pseq(#[2000, 1000, 800, 400], inf), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, tmax6 + (0.08*sinFunc)+(0.10*cosFunc), \speed, 2+((10*rampFunc*0.20*coeff)/durb2), \len, 0.20*coeff,
				\dur, Pseq([0.20*coeff], durb2/(0.20*coeff)), \pan, (0.4+(((0.08*sinFunc)+(0.10*cosFunc))*((10*rampFunc*0.20*coeff)/durb2))), \vol, ((-0.7*sinFunc)+(0.5*cosFunc))/(1+((100*rampFunc*0.20*coeff)/durb2)), \envIdx, 1);

			Ptpar([0.00, cbp1, durb1, cbp4    ], 1);
		};

		//-----Cellc6 : constante transposee
		cellc6 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

			lhx = 0.1*(max(lh6, (1-lh6))); coeff = (l2/(2*l3));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, tmx6 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx1/(0.20*coeff)), \pan, (0.8-((10*rampFunc*0.20*coeff)/durx1)-compFunc1), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);
			cxp4 = Pbind(\instrument, \instRLPF, \filtFrq, 2000 +(400*squared(0.7*sinFunc))-(1000*cosFunc), \filtQ, 0.5, \bufNum, soundFile6.bufnum, \start, tmx6 + (0.09*sinFunc)-(0.10*cosFunc), \speed, (3.5*(fmin1/fmin5))+(0.01*sinFunc)-(0.01*cosFunc),
				\len, 0.20*coeff, \dur, Pseq([0.20*coeff], durx2/(0.20*coeff)), \pan, Pseq(#[-1, 1, 0], inf), \vol, 0.6+(-0.2*sinFunc)+(0.1*cosFunc), \envIdx, 1);

			Ptpar([0.00, cxp1, durx1, cxp4 ], 1);
		};

		//-----Partitur de voix 6 du canon 4
		// Ptpar([0.00, cella6.value], 1);

		Ptpar([
			czeita6, cella6.value,
			czeitb6, cellb6.value,
			czeitc6, cellc6.value,
			czeitd6, celld6.value,
			czeite6, celle6.value,
			czeitf6, cellf6.value,
			czeitg6, cellg6.value,
			czeith6, cellh6.value,
			czeiti6, celli6.value
			], 1);
	};

	// -----------------------------------
	//----------citation voix VII -----------
	// -----------------------------------

	zitat = {
		var czeitav,
		zitata, zitatb, zitatc, zitatd, zitate, zitatf, zitatg, zitath, zitati,           //voix VII
		zizeita, zizeitb, zizeitc, zizeitd, zizeite, zizeitf, zizeitg, zizeith, zizeiti     ;

		//------partition du temps
		czeitav = 0;
		zizeitb = czeitav + (3*pulsz);   // voix VII
		zizeitc = czeitav + (18*pulsz);
		zizeitd = czeitav + (28*pulsz);
		zizeite = czeitav + (48*pulsz);
		zizeitf = czeitav + (68*pulsz);
		zizeitg = czeitav + (78*pulsz);
		zizeith = czeitav + (93*pulsz);
		zizeiti = czeitav + (113*pulsz);
		zizeita = czeitav + (118*pulsz);

		//-------------------- Voix VII citations ---------------------

		//-----zitatb : citation
		zitatb = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
			lhx = min((lh5/10), ((10-lh5)/1));
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);

			cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile5.bufnum, \bufNum2, soundFile6.bufnum, \start, (min(tmin5, tmin6) + (0.1*(l5/l6)*cosFunc)), \speed, (fmin5/fmin6), \dur, Pseq([0.2*(l2/l3)], durx1/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.2*(l2/l3))*rampFunc/durx1), \vol, (10*(0.2*(l2/l3))*rampFunc/durx1), \envIdx, 3);
			cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile5.bufnum, \bufNum2, soundFile6.bufnum, \start, (min(tmax5, tmax6) + (0.1*(l6/l5)*cosFunc)), \speed, (fmax5/fmax6), \dur, Pseq([0.2*(l2/l3)], durx2/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.2*(l2/l3))*rampFunc/durx2)), \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
		};
		//-----zitatc : citation
		zitatc = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
			lhx = min((lh3/10), ((10-lh3)/1));
			durx1 = 2.5*pulsz*lhx; durx2 = 2.5*pulsz*(1-lhx);

			cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile3.bufnum, \bufNum2, soundFile4.bufnum, \start, (min(tmin3, tmin4) + (0.1*(l3/l4)*cosFunc)), \speed, (fmin3/fmin4), \dur, Pseq([0.2*(l2/l3)], durx1/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.2*(l2/l3))*rampFunc/durx1), \vol, (10*(0.2*(l2/l3))*rampFunc/durx1), \envIdx, 3);
			cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile3.bufnum, \bufNum2, soundFile4.bufnum, \start, (min(tmx3, tmx4) + (0.1*(l3/l4)*cosFunc)), \speed, (fmax3/fmax4), \dur, Pseq([0.2*(l2/l3)], durx2/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.2*(l2/l3))*rampFunc/durx2)), \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
		};
		//-----zitatd : citation
		zitatd = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
			lhx = min((lh1/10), ((10-lh1)/1));
			durx1 = 2*pulsz*lhx; durx2 = 2*pulsz*(1-lhx);

			cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile4.bufnum, \bufNum2, soundFile1.bufnum, \start, (min(tmin1, tmin4) + (0.1*(l4/l1)*cosFunc)), \speed, (fmin4/fmin1), \dur, Pseq([0.2*(l2/l3)], durx1/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.2*(l2/l3))*rampFunc/durx1), \vol, (10*(0.2*(l2/l3))*rampFunc/durx1), \envIdx, 3);
			cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile4.bufnum, \bufNum2, soundFile1.bufnum, \start, (min(tmax1, tmax4) + (0.1*(l4/l1)*cosFunc)), \speed, (fmax4/fmax1), \dur, Pseq([0.2*(l2/l3)], durx2/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.2*(l2/l3))*rampFunc/durx2)), \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
		};
		//-----zitate : citation
		zitate = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
			lhx = min((lh2/10), ((10-lh2)/1));
			durx1 = 3*pulsz*lhx; durx2 = 3*pulsz*(1-lhx);

			cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile2.bufnum, \bufNum2, soundFile6.bufnum, \start, (min(tmin2, tmin6) + (0.1*(l2/l6)*cosFunc)), \speed, (fmx2/fmx6), \dur, Pseq([0.2*(l2/l3)], durx1/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.2*(l2/l3))*rampFunc/durx1), \vol, (10*(0.2*(l2/l3))*rampFunc/durx1), \envIdx, 3);
			cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile2.bufnum, \bufNum2, soundFile6.bufnum, \start, (min(tmax2, tmax6) + (0.1*(l6/l2)*cosFunc)), \speed, (fmin2/fmin6), \dur, Pseq([0.2*(l2/l3)], durx2/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.2*(l2/l3))*rampFunc/durx2)), \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
		};
		//-----zitatf : citation
		zitatf = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
			lhx = min((lh5/10), ((10-lh5)/1));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);

			cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile5.bufnum, \bufNum2, soundFile1.bufnum, \start, (min(tmin5, tmin1) + (0.1*(l5/l1)*cosFunc)), \speed, (fmin5/fmin1), \dur, Pseq([0.2*(l2/l3)], durx1/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.2*(l2/l3))*rampFunc/durx1), \vol, (10*(0.2*(l2/l3))*rampFunc/durx1), \envIdx, 3);
			cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile1.bufnum, \bufNum2, soundFile5.bufnum, \start, (min(tmax5, tmax1) + (0.1*(l1/l5)*cosFunc)), \speed, (fmax5/fmax1), \dur, Pseq([0.2*(l2/l3)], durx2/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.2*(l2/l3))*rampFunc/durx2)), \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
		};
		//-----zitatg : citation
		zitatg = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
			lhx = min((lh4/10), ((10-lh4)/1));
			durx1 = 4*pulsz*lhx; durx2 = 4*pulsz*(1-lhx);

			cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile4.bufnum, \bufNum2, soundFile3.bufnum, \start, (min(tmin3, tmin4) + (0.1*(l4/l3)*cosFunc)), \speed, (fmin3/fmin4), \dur, Pseq([0.2*(l2/l3)], durx1/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.2*(l2/l3))*rampFunc/durx1), \vol, (10*(0.2*(l2/l3))*rampFunc/durx1), \envIdx, 3);
			cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile4.bufnum, \bufNum2, soundFile3.bufnum, \start, (min(tmax3, tmax4) + (0.1*(l3/l4)*cosFunc)), \speed, (fmax3/fmax4), \dur, Pseq([0.2*(l2/l3)], durx2/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.2*(l2/l3))*rampFunc/durx2)), \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
		};
		//-----zitath : citation
		zitath = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
			lhx = min((lh6/10), ((10-lh6)/1));
			durx1 = 5*pulsz*lhx; durx2 = 5*pulsz*(1-lhx);

			cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile1.bufnum, \bufNum2, soundFile6.bufnum, \start, (min(tmx1, tmx6) + (0.1*(l6/l1)*cosFunc)), \speed, (fmin6/fmin1), \dur, Pseq([0.1*(l2/l3)], durx1/(0.1*(l2/l3))), \len, 0.1*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.1*(l2/l3))*rampFunc/durx1), \vol, (10*(0.1*(l2/l3))*rampFunc/durx1), \envIdx, 3);
			cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile6.bufnum, \bufNum2, soundFile1.bufnum, \start, (min(tmax1, tmax6) + (0.1*(l1/l6)*cosFunc)), \speed, (fmax6/fmax1), \dur, Pseq([0.1*(l2/l3)], durx2/(0.1*(l2/l3))), \len, 0.1*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.1*(l2/l3))*rampFunc/durx2)), \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
		};
		//-----zitati : citation
		zitati = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
			lhx = min((lh2/10), ((10-lh2)/1));
			durx1 = 5*pulsz*lhx; durx2 = 5*pulsz*(1-lhx);

			cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile2.bufnum, \bufNum2, soundFile4.bufnum, \start, (min(tmx4, tmx2) + (0.1*(l2/l4)*cosFunc)), \speed, (fmin4/fmin2), \dur, Pseq([0.2*(l2/l3)], durx1/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.2*(l2/l3))*rampFunc/durx1), \vol, (10*(0.2*(l2/l3))*rampFunc/durx1), \envIdx, 3);
			cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile2.bufnum, \bufNum2, soundFile4.bufnum, \start, (min(tmax2, tmax4) + (0.1*(l2/l4)*cosFunc)), \speed, (fmax2/fmax4), \dur, Pseq([0.2*(l2/l3)], durx2/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.2*(l2/l3))*rampFunc/durx2)), \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
		};
		//-----zitata : citation
		zitata = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx;
			lhx = min((lh3/10), ((10-lh3)/1));
			durx1 = 10*pulsz*lhx; durx2 = 10*pulsz*(1-lhx);

			cxp1 = Pbind( \instrument, \instXFade, \bufNum1, soundFile3.bufnum, \bufNum2, soundFile2.bufnum, \start, (min(tmax3, tmax2) + (0.1*(l3/l2)*cosFunc)), \speed, (fmax3/fmax2), \dur, Pseq([0.2*(l2/l3)], durx1/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, ((-0.4*sinFunc)+(0.7*cosFunc))*(10*(0.2*(l2/l3))*rampFunc/durx1), \vol, (10*(0.2*(l2/l3))*rampFunc/durx1), \envIdx, 3);
			cxp3 = Pbind( \instrument, \instXFade, \bufNum1, soundFile3.bufnum, \bufNum2, soundFile2.bufnum, \start, (min(tmx2, tmx3) + (0.1*(l3/l2)*cosFunc)), \speed, (fmx3/fmx2), \dur, Pseq([0.2*(l2/l3)], durx2/(0.2*(l2/l3))), \len, 0.2*(l2/l3), \theCrossFreq, (3+(0.5*cosFunc)),  \pan, (0.5*sinFunc)+(-0.7*cosFunc), \vol, (1-(10*(0.2*(l2/l3))*rampFunc/durx2)), \envIdx, 3);

			Ptpar([0.00, cxp1, durx1, cxp3 ], 1);
		};


		//-----Partitur de voix VII zitat du canon 4
		//    Ptpar([0.00, zitati.value], 1);

		Ptpar([
			zizeitb, zitatb.value,
			zizeitc, zitatc.value,
			zizeitd, zitatd.value,
			zizeite, zitate.value,
			zizeitf, zitatf.value,
			zizeitg, zitatg.value,
			zizeith, zitath.value,
			zizeiti, zitati.value,
			zizeita, zitata.value
			], 1);
	};

	// -----------------------------------
	//----------voix 8 du canon 4 ---------
	// -----------------------------------
	voix8= {
		var czeitav,
		cella8, cellb8, cellc8, celld8, celle8, cellf8, cellg8, cellh8, celli8,      // voix 8
		czeita8, czeitb8, czeitc8, czeitd8, czeite8, czeitf8, czeitg8, czeith8, czeiti8,
		p1, p2, p3, p4, p5, p6
		;

		//------partition du temps
		czeitav = 0;
		czeita8 = czeitav + (100*pulsz); // a debute a 100 (les voix 1 a 7 elles ont commencees au debut)
		czeitb8 = czeita8 + (8*pulsz); // demarre 8 apres a mais termine avec a a 134
		czeitc8 = czeita8 + (20*pulsz); // demarre 20 apres a mais termine avec a a 134
		// 134 est egalement fin des autres voix du canon
		czeitd8 = czeita8 + (34*pulsz); //demarre a 134 jusqu a 164
		czeite8 = czeita8 + (44*pulsz); // demarre 144 jusqu a 154
		czeitf8 = czeita8 + (64*pulsz); // troisieme section canon demarre 164 jusqu a 174
		czeitg8 = czeita8 + (74*pulsz); // demare 174; Total voix 8 100 (debut a) +74 (a +b+c+d+e+f)+ 16 desinence g

		p1 = max (lh1.floor, 1);  p2 = max (lh2.floor, 1);  p3 = max (lh3.floor, 1); p4 = max (lh4.floor, 1); p5 = max (lh5.floor, 1); p6 = max (lh6.floor, 1);
		//-------------------- Voix VIII ---------------------
		cella8 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			coeff = (l2/l3);
			durx1 = 34*pulsz;  //duree locales entre debut et fin de cellule -ancienne valeur-

			cxp1 = Pbind(\instrument, \instFunc,\bufNum, Pxrand([soundFile1.bufnum, soundFile2.bufnum, soundFile3.bufnum, soundFile4.bufnum, soundFile5.bufnum, soundFile6.bufnum], inf),\start, ((rrand(min(tmax3, tmx4), min(tmax1, tmx6), min(tmax5, tmx2), min(tmax6, tmx3))) + (0.5*sinFunc)+(-0.4*cosFunc)),\speed, 1.03+ (0.7*(sinFunc*cosFunc)), \freq, 200*(40+ 30*cosFunc),\q, 4,\len, (0.4*coeff),\dur,  Pseq([0.4*coeff], durx1/(0.4*coeff)), \pan, (0.4*sinFunc)+(-0.3*cosFunc),\vol,  0.6+(0.4*sinFunc)+(-0.3*(cosFunc*cosFunc+0.2)),\envIdx, 1);

			Ptpar([0.00,  cxp1], 1);
		};

		//----------------
		cellb8 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;
			coeff = (l2/l3);
			durx1 = 26*pulsz;  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instFunc,\bufNum, Pseq([soundFile3.bufnum, soundFile1.bufnum, soundFile4.bufnum, soundFile2.bufnum, soundFile6.bufnum, soundFile5.bufnum], inf), \speed, (1.2+(-0.6*((sinFunc+1)*cosFunc))),\freq, 1600*(30-20*sinFunc),\q, 1,\frqEnvIdx, 1,\start, (rrand(min(tmax3, tmin4), min(tmax1, tmin6), min(tmax5, tmin2), min(tmax6, tmin3))+ (0.9*cosFunc)+(3*sinFunc)), \len, (0.2*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.9*sinFunc)+(-0.8*cosFunc),\vol, 0.6+(0.3*sinFunc)+(-0.2*(cosFunc*sinFunc-0.3)),\envIdx, 1);
			cxp2 = Pbind(\instrument, \instFunc,\bufNum, Pseq([soundFile1.bufnum, soundFile2.bufnum, soundFile3.bufnum, soundFile4.bufnum, soundFile5.bufnum, soundFile6.bufnum], inf),\speed, (0.9-(0.3*((sinFunc-0.5)*(cosFunc+0.5)))),\freq, 18000*(-20+20*sinFunc),\q, 1,\frqEnvIdx, 0, \len, (0.8*coeff),\start, (rrand(min(tmx3, tmin1), max(tmax1, tmin3), min(tmx5, tmin4), min(tmax6, tmin2))+ (3*cosFunc)+(4*sinFunc)),\dur, Pseq([0.8*coeff], durx1/(0.8*coeff)),\pan, (-0.9*sinFunc)+(0.8*cosFunc),\vol, 0.7+(-0.6*sinFunc*sinFunc)+(0.7*cosFunc),\envIdx, 1);

			cxp3 = Pbind(\instrument, \instFunc,\bufNum, Pseq([soundFile4.bufnum, soundFile2.bufnum, soundFile3.bufnum, soundFile5.bufnum, soundFile6.bufnum, soundFile1.bufnum], inf),\speed, Pseq([ Pgeom(0.8, 1.3, 21), Pgeom(0.75, 0.7, 32) ], inf),\start, (rrand(min(tmin2, tmin1), max(tmin3, tmin2), min(tmin5, tmin3), max(tmin2, tmin5)) + (-0.6*cosFunc)+(3*sinFunc)), \len, (0.2*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\freq, 150*(47+20*sinFunc),\q, 1,\frqEnvIdx, 0,\pan, (0.8*sinFunc)+(-0.4*cosFunc),\vol, 0.1+(0.4*sinFunc)+(-0.1*cosFunc),\envIdx, 1);
			cxp4 = Pbind(\instrument, \instFunc,\bufNum, Pseq([soundFile6.bufnum, soundFile1.bufnum, soundFile4.bufnum, soundFile3.bufnum, soundFile2.bufnum, soundFile5.bufnum], inf),\speed, Pseq([ Pgeom(0.8, 1.2, 24), Pgeom(1.1, 0.909, 24) ], inf),\start, (rrand(min(tmin3, tmin2), max(tmin1, tmin3), min(tmin4, tmin5), max(tmin2, tmax6)) + (0.6*cosFunc)+(-3*sinFunc)), \len, (0.2*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\freq, 150*(47+20*sinFunc),\q, 1,\frqEnvIdx, 0,\pan, (-0.8*sinFunc)+(-0.4*cosFunc),\vol, 0.1+(-0.3*sinFunc)+(0.2*cosFunc),\envIdx, 1);

			Ptpar([0.00,  cxp1, 0.00, cxp2, 0.00, cxp3, 0.00,cxp4], 1);
		};

		//----------------
		cellc8 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, cxp7, cxp8, cxp9, durx1, durx2, lhx, coeff;
			coeff = (l2/l3);
			durx1 = 14*pulsz;  //duree locales entre debut et fin de cellule

			cxp1 = Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum, \speed, (1.2+(0.3*(((-1*sinFunc)+1)*cosFunc))), \start, (tmax3 +(-1*sinFunc)+(2*cosFunc)),\freq, 1600,\q, 1,\frqEnvIdx, 1, \len, (0.41*coeff),\dur, Pseq([0.4*coeff], durx1/(0.4*coeff)),\pan, (0.9*sinFunc)+(-0.8*cosFunc),\vol, (0.2+(0.9*cosFunc*sinFunc)+(0.8*sinFunc)),\envIdx, 1);
			cxp2 = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\speed, (0.9+(-0.4*((sinFunc+1)*(-1*cosFunc)))),\start, (tmx1 +(3*sinFunc)+(-4*cosFunc)),\freq, 1400,\q, 1,\frqEnvIdx, 1, \len, (0.22*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (-0.9*sinFunc)+(0.8*cosFunc),\vol, (0.2+(0.6*(cosFunc-0.2)*sinFunc)+(0.4*sinFunc)),\envIdx, 1);
			cxp3 = Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\speed, (1.1+(0.5*((sinFunc-1)*sinFunc))), \start, (tmax5+(-2*sinFunc)+(-3*cosFunc)),\freq, 1000,\q, 1,\frqEnvIdx, 1, \len, (0.23*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.7*sinFunc)+(0.6*cosFunc),\vol, (0.2+(-0.6*sinFunc)+(0.5*(sinFunc+0.5)*cosFunc)),\envIdx, 1);
			cxp4 = Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\speed, (0.8-(0.2*((cosFunc+1)*sinFunc))),\start, (tmin6+(4*sinFunc)+(3*cosFunc)),\freq, 1800,\q, 1,\frqEnvIdx, 1, \len, (0.41*coeff),\dur, Pseq([0.6*coeff], durx1/(0.6*coeff)),\pan, (-0.8*sinFunc)+(-0.4*cosFunc),\vol, (0.1+(0.3*sinFunc)+(0.3*cosFunc*sinFunc)),\envIdx, 1);

			//montee geometrique puls double
			cxp5 = Pbind(\instrument, \instFunc,\bufNum, Pseq([soundFile2.bufnum, soundFile3.bufnum, soundFile1.bufnum, soundFile5.bufnum, soundFile4.bufnum, soundFile6.bufnum], inf),\speed, Pseq([ Pgeom(0.67, 1.5, 19), Pgeom(0.87, 1.99, 35) ], inf),\start, (rrand(min(tmin2, tmin3), max(tmin2, tmin4), max(tmin4, tmin3), max(tmin2, tmin5)) + (-0.4*cosFunc)+(3*sinFunc)), \len, (0.2*coeff),\dur, Pseq([0.1*coeff], durx1/(0.1*coeff)),\freq, 150*(47+20*sinFunc),\q, 1,\frqEnvIdx, 0,\pan, (0.6*sinFunc)+(-0.3*cosFunc),\vol, 0.1+(0.2*sinFunc)+(-0.1*cosFunc),\envIdx, 1);
			cxp6 = Pbind(\instrument, \instFunc,\bufNum, Pseq([soundFile6.bufnum, soundFile5.bufnum, soundFile3.bufnum, soundFile4.bufnum, soundFile1.bufnum, soundFile2.bufnum], inf),\speed, Pseq([ Pgeom(0.6, 1.3, 27), Pgeom(1.3, 0.9, 13) ], inf),\start, (rrand(min(tmin1, tmin2), max(tmin4, tmin3), min(tmin2, tmin5), max(tmin3, tmax6)) + (0.6*cosFunc)+(-3*sinFunc)), \len, (0.2*coeff),\dur, Pseq([0.1*coeff], durx1/(0.1*coeff)),\freq, 150*(47+20*sinFunc),\q, 1,\frqEnvIdx, 0,\pan, (0.3*sinFunc)+(-0.4*cosFunc),\vol, 0.1+(0.2*sinFunc)+(-0.1*cosFunc),\envIdx, 1);

			//modulation en anneau aleatoire pour incise l�g�re duree dix pulsz
			cxp7 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, (l2/2)+((l2/4)*sinFunc), \speed, 1.2, \len, 0.072, \dur,Pseq([(0.2/3)*coeff], 10*pulsz/((0.2/3)*coeff)), \pan, 0.8*(sinFunc-cosFunc), \vol, (((1/3)*coeff/10*pulsz)*rampFunc), \envIdx, 3);
			cxp8 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, (l4/2)+((l4/4)*sinFunc), \speed, 1.5, \len, 0.072, \dur,Pseq([(0.1)*coeff], 10*pulsz/((0.1)*coeff)), \pan, 0.8*(sinFunc-cosFunc), \vol, ((0.5*coeff/10*pulsz)*rampFunc), \envIdx, 3);
			//modulation en anneau aleatoire pour incise duree deux pulsz
			cxp9 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, (l3/2)+((l3/4)*cosFunc), \speed, 0.9, \len, 0.075, \dur,Pseq([0.1*coeff], 2*pulsz/(0.1*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, ((1*coeff/2*pulsz)*rampFunc), \envIdx, 3);

			Ptpar([0.00,  cxp1, 0.00, cxp2, 0.00, cxp3, 0.00, cxp4, 0.00, cxp5, 0.00, cxp6, (4*pulsz), cxp7, (4*pulsz), cxp8, (12*pulsz), cxp9], 1);
		};
		//---------------- // petite coda d reprend cellc8 en doublant le rythme
		celld8 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, cxp7, cxp8, cxp9, durx1, durx2, lhx, coeff;
			coeff = max((l2/(2*l3)), (l3/(2*l2)));
			durx1 = 30*pulsz;  //duree locales entre debut et fin de cellule ancienne valeur 10

			cxp1 = Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum, \speed,((1.2+(0.3*(((-1*sinFunc)+1)*cosFunc)))- ((1*rampFunc*0.4*coeff)/durx1)), \start, (tmax3 +(-2*sinFunc)+(2*cosFunc)),\freq, 1600,\q, 1,\frqEnvIdx, 1, \len, (0.4*coeff),\dur, Pseq([0.4*coeff], durx1/(0.4*coeff)),\pan, (-0.9*sinFunc)+(0.8*cosFunc),\vol, 0.5+(0.7*(cosFunc+0.4)*sinFunc)+(-0.5*(sinFunc+0.2)*cosFunc) - ((0.7*rampFunc*0.4*coeff)/durx1),\envIdx, 1);
			cxp2 = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\speed, (0.9+(-0.4*((sinFunc+1)*(-1*cosFunc)))) + ((1*rampFunc*0.2*coeff)/durx1),\start, tmx1+(-2*sinFunc)+(2*cosFunc),\freq, 14000,\q, 1,\frqEnvIdx, 1, \len, (0.25*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.9*sinFunc)+(-0.8*cosFunc),\vol, 0.5+(-0.6*cosFunc*sinFunc)+(0.7*sinFunc) - ((0.7*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp3 = Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\speed, (1.1+(0.5*((sinFunc-1)*sinFunc))) - ((2*rampFunc*0.6*coeff)/durx1), \start, tmax5+(2*sinFunc)+(2*cosFunc),\freq, 1000,\q, 1,\frqEnvIdx, 1, \len, (0.25*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (-0.7*sinFunc)+(-0.6*cosFunc),\vol, 0.5+(-0.7*sinFunc*sinFunc)+(-0.6*sinFunc) - ((0.7*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp4 = Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\speed, (0.8-(0.3*((cosFunc+1)*sinFunc)))+ ((2*rampFunc*0.6*coeff)/durx1),\start, tmx6+(2*sinFunc)+(-2*cosFunc),\freq, 1800,\q, 1,\frqEnvIdx, 1, \len, (0.21*coeff),\dur, Pseq([0.6*coeff], durx1/(0.6*coeff)),\pan, (0.8*sinFunc)+(0.4*cosFunc),\vol, 0.5+(0.7*(sinFunc+0.2))+(0.7*sinFunc*cosFunc) - ((0.7*rampFunc*0.6*coeff)/durx1),\envIdx, 1);
			cxp5 = Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\speed, (1.25+(-0.7*((sinFunc+1)*cosFunc))) - ((1*rampFunc*0.2*coeff)/durx1),\start, tmax2 +(3*sinFunc)+(-1*cosFunc),\freq, 1500,\q, 1,\frqEnvIdx, 1, \len, (0.21*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.8*sinFunc)+(-0.9*cosFunc),\vol, 0.5+(-0.7*(cosFunc+0.1)*cosFunc)+(-0.5*sinFunc) - ((0.7*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp6 = Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum, \speed, (0.6-(0.8*((sinFunc+1)*cosFunc))) + ((1*rampFunc*0.2*coeff)/durx1), \start, tmx4 +(2*sinFunc)+(-2*cosFunc),\freq, 1700,\q, 1,\frqEnvIdx, 1, \len, (0.26*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (-0.7*sinFunc)+(-0.8*cosFunc),\vol, 0.5+(0.6*cosFunc*sinFunc)+(0.4*sinFunc) - ((0.7*rampFunc*0.2*coeff)/durx1),\envIdx, 1);

			//modulation en anneau aleatoire
			cxp7 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, (l2/2)+((l2/4)*sinFunc), \speed, 1.1*coeff, \len, 0.072, \dur,Pseq([(0.2/3)*coeff], durx1/((0.2/3)*coeff)), \pan, 0.8*(sinFunc-cosFunc), \vol, 0.7+(0.8*sinFunc*sinFunc)+(-0.4*cosFunc) - ((0.7*rampFunc*(0.2/3)*coeff)/durx1), \envIdx, 3);
			cxp8 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, (l4/2)+((l4/4)*sinFunc), \speed, 1.3*coeff, \len, 0.072, \dur,Pseq([(0.2)*coeff], durx1/((0.2)*coeff)), \pan, 0.8*(sinFunc-cosFunc), \vol, 0.8+(-0.7*cosFunc*cosFunc)+(0.4*sinFunc) - ((0.7*rampFunc*0.2*coeff)/durx1), \envIdx, 3);
			cxp9 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, (l3/2)+((l3/4)*cosFunc), \speed, (1.6/coeff), \len, 0.075, \dur,Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 0.8+(-0.6*sinFunc*cosFunc)+(-0.6*sinFunc) - ((0.7*rampFunc*0.2*coeff)/durx1), \envIdx, 3);

			Ptpar([0.00,  cxp1, 0.00, cxp2, 0.00, cxp3, 0.00, cxp4, 0.00, cxp5, 0.00, cxp6, 0.00, cxp7, 0.00, cxp8, 0.00, cxp9], 1);
		};

		//---------------- // petite coda de d 2e voix deux fois plus vite que d arrive a la fin
		celle8 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, cxp7, cxp8, cxp9, cxp10, cxp11, cxp12, cxp13, durx1, durx2, lx, coeff;
			coeff = max((l2/l3), (l3/l2)); lx = l3;
			durx1 = 20*pulsz;

			cxp1 = Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum, \speed,((1.3-(0.3*(((2*sinFunc)-1)*cosFunc))) + ((1*rampFunc*0.4*coeff)/durx1)), \start, (tmx3 +(2*sinFunc)+(-2*cosFunc)),\freq, 1600,\q, 1,\frqEnvIdx, 1, \len, (0.4*coeff),\dur, Pseq([0.4*coeff], durx1/(0.4*coeff)),\pan, (0.9*sinFunc)+(-0.8*cosFunc),\vol, 0.5+(-0.7*(cosFunc+0.4)*sinFunc)+(0.5*(sinFunc+0.2)*cosFunc) - ((0.7*rampFunc*0.4*coeff)/durx1),\envIdx, 1);
			cxp2 = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\speed, (1.4+(0.4*((sinFunc-1)*(1*cosFunc)))) - ((1*rampFunc*0.2*coeff)/durx1),\start, tmax1+(-2*sinFunc)+(-2*cosFunc),\freq, 14000,\q, 1,\frqEnvIdx, 1, \len, (0.2*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (-0.9*sinFunc)+(0.8*cosFunc),\vol, 0.5+(0.6*cosFunc*sinFunc)+(-0.7*sinFunc) + ((0.7*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp3 = Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\speed, (1.3+(-0.5*((sinFunc-1)*sinFunc))) + ((2*rampFunc*0.6*coeff)/durx1), \start, tmx5+(-2*sinFunc)+(-2*cosFunc),\freq, 1000,\q, 1,\frqEnvIdx, 1, \len, (0.2*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.7*sinFunc)+(-0.6*cosFunc),\vol, 0.5+(0.7*sinFunc*sinFunc)+(0.6*sinFunc) + ((0.7*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp4 = Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\speed, (1.2+(0.3*((cosFunc+1)*sinFunc))) - ((2*rampFunc*0.6*coeff)/durx1),\start, tmax6+(-2*sinFunc)+(2*cosFunc),\freq, 1800,\q, 1,\frqEnvIdx, 1, \len, (0.3*coeff),\dur, Pseq([0.6*coeff], durx1/(0.6*coeff)),\pan, (-0.8*sinFunc)+(0.4*cosFunc),\vol, 0.5-(0.7*(sinFunc+0.2))+(-0.7*sinFunc*cosFunc) + ((0.7*rampFunc*0.6*coeff)/durx1),\envIdx, 1);
			cxp5 = Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\speed, (1.3+(-0.7*((sinFunc+1)*cosFunc))) + ((1*rampFunc*0.2*coeff)/durx1),\start, tmx2 +(-3*sinFunc)+(1*cosFunc),\freq, 1500,\q, 1,\frqEnvIdx, 1, \len, (0.21*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (-0.8*sinFunc)+(-0.9*cosFunc),\vol, 0.5+(-0.7*(cosFunc+0.1)*cosFunc)+(-0.5*sinFunc) + ((0.7*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp6 = Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum, \speed, (0.9+(0.8*((sinFunc-1)*cosFunc))) - ((1*rampFunc*0.2*coeff)/durx1), \start, tmax4 -(2*sinFunc)+(2*cosFunc),\freq, 1700,\q, 1,\frqEnvIdx, 1, \len, (0.26*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.7*sinFunc)+(-0.8*cosFunc),\vol, 0.5+(-0.6*cosFunc*sinFunc)+(0.4*sinFunc) + ((0.7*rampFunc*0.2*coeff)/durx1),\envIdx, 1);

			//modulation en anneau aleatoire
			cxp7 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, (l2/2)+((l2/4)*sinFunc), \speed, 1.2*coeff, \len, 0.072, \dur,Pseq([(0.2/3)*coeff], durx1/((0.2/3)*coeff)), \pan, -0.8*(sinFunc+cosFunc), \vol, 0.8+(0.1*sinFunc*sinFunc)+(-0.4*cosFunc) + ((0.2*rampFunc*(0.2/3)*coeff)/durx1), \envIdx, 3);
			cxp8 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, (l4/2)+((l4/4)*sinFunc), \speed, 1.3*coeff, \len, 0.072, \dur,Pseq([(0.2)*coeff], durx1/((0.2)*coeff)), \pan, -0.8*(sinFunc-cosFunc), \vol, 0.9+(-0.3*cosFunc*cosFunc)+(0.2*sinFunc) + ((0.2*rampFunc*0.2*coeff)/durx1), \envIdx, 3);
			cxp9 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, (l3/2)+((l3/4)*cosFunc), \speed, (1.5/coeff), \len, 0.075, \dur,Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 0.9+(-0.4*sinFunc*cosFunc)+(-0.3*sinFunc) + ((0.2*rampFunc*0.2*coeff)/durx1), \envIdx, 3);
			// courtes a la fin
			cxp10 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmx1+((l1/4)*sinFunc*cosFunc), \speed, (1.1), \len, (0.1*coeff), \dur,Pseq([0.05*coeff], (5*pulsz)/(0.05*coeff)), \pan, 0.7*(2*cosFunc+sinFunc), \vol, 1, \envIdx, 1);
			cxp11 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, tmax6+((l6/4)*(cosFunc-1.5)), \speed, (0.8), \len, (0.1*coeff), \dur,Pseq([0.05*coeff], (5*pulsz)/(0.05*coeff)), \pan, 0.3*(cosFunc-(cosFunc*sinFunc)), \vol, 1, \envIdx, 1);
			cxp12 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, tmx4+((l4/4)*(sinFunc+1.5)), \speed, (0.6), \len, (0.1*coeff), \dur,Pseq([0.05*coeff], (5*pulsz)/(0.05*coeff)), \pan, -0.3*(sinFunc-(cosFunc*sinFunc)), \vol, 1, \envIdx, 1);
			cxp13 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, tmax3+((l3/4)*sinFunc*cosFunc), \speed, (1.4), \len, (0.1*coeff), \dur,Pseq([0.05*coeff], (4*pulsz)/(0.05*coeff)), \pan, -0.7*(3*sinFunc-cosFunc), \vol, 1, \envIdx, 1);


			Ptpar([0.00,  cxp1, 0.00, cxp2, 0.00, cxp3, 0.00, cxp4, 0.00, cxp5, 0.00, cxp6, 0.00, cxp7, 0.00, cxp8, 0.00, cxp9, (durx1-(5*pulsz)), cxp10, (durx1-(5*pulsz)), cxp11, (durx1-(5*pulsz)), cxp12, (durx1-(4*pulsz)), cxp13], 1);
		};

		//---------------- // petite coda de d 2e voix quatre fois plus vite que d arrive a la fin
		cellf8 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, cxp7, cxp8, cxp9, cxp10, cxp11, cxp12, cxp13, durx1, durx2, lx, coeff;
			coeff = max((l2/(2.1*l3)), (l3/(2.2*l2))); lx = l3;
			durx1 = 10*pulsz;

			cxp1 = Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum, \speed,((1.3-(0.5*(((2*sinFunc)-0.3)*cosFunc))) + ((1*rampFunc*0.4*coeff)/durx1)), \start, (tmx3 +(2*sinFunc)+(-2*cosFunc)),\freq, 1600,\q, 1,\frqEnvIdx, 1, \len, (0.42*coeff),\dur, Pseq([0.4*coeff], durx1/(0.4*coeff)),\pan, (0.9*sinFunc)+(-0.8*cosFunc),\vol, 0.9+(-0.4*(cosFunc+0.4)*sinFunc)+(0.5*(sinFunc+0.2)*cosFunc) - ((0.1*rampFunc*0.4*coeff)/durx1),\envIdx, 1);
			cxp2 = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\speed, (1.4+(0.7*((sinFunc-0.9)*(1*cosFunc)))) - ((1*rampFunc*0.2*coeff)/durx1),\start, tmax1+(-2*sinFunc)+(-2*cosFunc),\freq, 14000,\q, 1,\frqEnvIdx, 1, \len, (0.22*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (-0.9*sinFunc)+(0.8*cosFunc),\vol, 0.9+(0.45*cosFunc*sinFunc)+(-0.4*sinFunc) + ((0.1*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp3 = Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\speed, (1.3+(-0.6*((sinFunc-0.7)*sinFunc))) + ((2*rampFunc*0.6*coeff)/durx1), \start, tmx5+(-2*sinFunc)+(-2*cosFunc),\freq, 1000,\q, 1,\frqEnvIdx, 1, \len, (0.22*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.7*sinFunc)+(-0.6*cosFunc),\vol, 0.9+(0.4*sinFunc*sinFunc)+(0.3*sinFunc) + ((0.1*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp4 = Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\speed, (1.2+(0.5*((cosFunc+0.1)*sinFunc))) - ((2*rampFunc*0.6*coeff)/durx1),\start, tmax6+(-2*sinFunc)+(2*cosFunc),\freq, 1800,\q, 1,\frqEnvIdx, 1, \len, (0.31*coeff),\dur, Pseq([0.3*coeff], durx1/(0.3*coeff)),\pan, (-0.8*sinFunc)+(0.4*cosFunc),\vol, 0.9-(0.4*(sinFunc+0.2))+(-0.5*sinFunc*cosFunc) + ((0.1*rampFunc*0.6*coeff)/durx1),\envIdx, 1);
			cxp5 = Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\speed, (1.3+(-0.7*((sinFunc+1.2)*cosFunc))) + ((1*rampFunc*0.2*coeff)/durx1),\start, tmx2 +(-3*sinFunc)+(1*cosFunc),\freq, 1500,\q, 1,\frqEnvIdx, 1, \len, (0.21*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (-0.8*sinFunc)+(-0.9*cosFunc),\vol, 0.9+(-0.4*(cosFunc+0.1)*cosFunc)+(-0.5*sinFunc) + ((0.1*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp6 = Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum, \speed, (0.9+(0.8*((cosFunc-0.3)*cosFunc))) - ((1*rampFunc*0.2*coeff)/durx1), \start, tmax4 -(2*sinFunc)+(2*cosFunc),\freq, 1700,\q, 1,\frqEnvIdx, 1, \len, (0.26*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.7*sinFunc)+(-0.8*cosFunc),\vol, 0.9+(-0.3*cosFunc*sinFunc)+(0.4*sinFunc) + ((0.1*rampFunc*0.2*coeff)/durx1),\envIdx, 1);

			//modulation en anneau aleatoire
			cxp7 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmx2+((l2/4)*sinFunc), \speed, 1.4, \len, 0.15*coeff, \dur, Pseq([(0.2/3)*coeff], durx1/((0.2/3)*coeff)), \pan, -0.8*(sinFunc+cosFunc), \vol, 1+(0.1*sinFunc*sinFunc)+(-0.4*cosFunc) + ((0.2*rampFunc*(0.2/3)*coeff)/durx1), \envIdx, 3);
			cxp8 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, tmax4+((l4/4)*sinFunc), \speed, 1.6, \len, 0.22*coeff, \dur,Pseq([(0.2)*coeff], durx1/((0.2)*coeff)), \pan, -0.8*(sinFunc-cosFunc), \vol, 1+(-0.3*cosFunc*cosFunc)+(0.2*sinFunc) + ((0.2*rampFunc*0.2*coeff)/durx1), \envIdx, 3);
			cxp9 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, tmx3+((l3/4)*cosFunc), \speed, 1.8, \len, 0.23*coeff, \dur,Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, 0.7*(cosFunc+sinFunc), \vol, 1+(-0.3*sinFunc*cosFunc)+(-0.3*sinFunc) - ((10*rampFunc*0.2*coeff)/durx1), \envIdx, 3);

			// courtes a la fin
			cxp10 = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmx1+((l1/4)*sinFunc*cosFunc), \speed, (1.3), \len, (0.1*coeff), \dur,Pseq([0.1*coeff], (3*pulsz)/(0.1*coeff)), \pan, 0.7*(2*cosFunc+sinFunc), \vol, 1, \envIdx, 1);
			cxp11 = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, tmax6+((l6/4)*(cosFunc-1.5)), \speed, (0.7), \len, (0.1*coeff), \dur,Pseq([0.1*coeff], (3*pulsz)/(0.1*coeff)), \pan, 0.3*(cosFunc-(cosFunc*sinFunc)), \vol, 1, \envIdx, 1);
			cxp12 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, tmx4+((l4/4)*(sinFunc+1.5)), \speed, (0.6), \len, (0.1*coeff), \dur,Pseq([0.1*coeff], (3*pulsz)/(0.1*coeff)), \pan, -0.3*(sinFunc-(cosFunc*sinFunc)), \vol, 1, \envIdx, 1);
			cxp13 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, tmax3+((l3/4)*sinFunc*cosFunc), \speed, (1.5), \len, (0.1*coeff), \dur,Pseq([0.1*coeff], (3*pulsz)/(0.1*coeff)), \pan, -0.7*(3*sinFunc-cosFunc), \vol, 1, \envIdx, 1);


			Ptpar([0.00,  cxp1, 0.00, cxp2, 0.00, cxp3, 0.00, cxp4, 0.00, cxp5, 0.00, cxp6, 0.00, cxp7, 0.00, cxp8, 0.00, cxp9, (durx1-(3*pulsz)), cxp10, (durx1-(3*pulsz)), cxp11, (durx1-(3*pulsz)), cxp12, (durx1-(3*pulsz)), cxp13], 1);
		};

		//---------------- // desinence finale duree 2 puls silence + durx1 reprise + durx2 desinence
		cellg8 = {
			var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, cxp1a, cxp2a, cxp3a, cxp4a, cxp5a, cxp6a, cxp7a, cxp8a, cxp9a, cxp7, cxp8, cxp9, cxp1d, cxp2d, cxp3d, cxp11, cxp12, cxp13, cxp14, cxp15, cxp16, durx1, durx2, lhx, coeff, coeff2, r;
			coeff = max((l2/(2.6*l3)), (l3/(2.8*l2)));
			coeff2 = (coeff/2);
			r=0.85; //longueur citation
			durx1 = 6*pulsz;
			durx2 = 8*pulsz;  //duree locales de la desinence entre debut et fin de cellule, la cellf dure durx2

			// citation tres courte comme signal cluster
			cxp11 = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\start, tmax1, \speed, 1,\len, r,\dur, Pseq([r], 1),\pan, -0.7,\vol, 0.9,\envIdx, 5);
			cxp12 = Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\start, tmax2, \speed, 1,\len, r,\dur, Pseq([r], 1),\pan, 0.5,\vol, 0.9,\envIdx, 5);
			cxp13 = Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum,\start, tmax3, \speed, 1,\len, r,\dur, Pseq([r], 1),\pan, 0,\vol, 0.9,\envIdx, 5);
			cxp14 = Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\start, tmax4, \speed, 1,\len, r,\dur, Pseq([r], 1),\pan, 0.2,\vol, 0.9,\envIdx, 5);
			cxp15 = Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\start, tmax5, \speed, 1,\len, r,\dur, Pseq([r], 1),\pan,-0.3,\vol, 0.9,\envIdx, 5);
			cxp16 = Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\start, tmax6, \speed, 1,\len, r,\dur, Pseq([r], 1),\pan, 0.8,\vol, 0.9,\envIdx, 5);

			// cxp11 a cxp16 cluster bref puis 2 pulsz de silence puis cxp1 a cxp 6 puis cxp1d a cxp4d desinence


			// reprise duree durx1
			cxp1 = Pbind(\instrument, \instFunc,\bufNum, soundFile3.bufnum, \speed,((1.3-(0.5*(((2*sinFunc)-0.3)*cosFunc))) + ((1*rampFunc*0.4*coeff)/durx1)), \start, (tmx3 +(2*sinFunc)+(-2*cosFunc)),\freq, 1600,\q, 1,\frqEnvIdx, 1, \len, (0.42*coeff),\dur, Pseq([0.4*coeff], durx1/(0.4*coeff)),\pan, (0.9*sinFunc)+(-0.8*cosFunc),\vol, 1+(-0.4*(cosFunc+0.4)*sinFunc)+(0.5*(sinFunc+0.2)*cosFunc) - ((0.1*rampFunc*0.4*coeff)/durx1),\envIdx, 1);
			cxp2 = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum,\speed, (1.4+(0.7*((sinFunc-0.9)*(1*cosFunc)))) - ((1*rampFunc*0.2*coeff)/durx1),\start, tmax1+(-2*sinFunc)+(-2*cosFunc),\freq, 14000,\q, 1,\frqEnvIdx, 1, \len, (0.22*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (-0.9*sinFunc)+(0.8*cosFunc),\vol, 1+(0.45*cosFunc*sinFunc)+(-0.4*sinFunc) + ((0.1*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp3 = Pbind(\instrument, \instFunc,\bufNum, soundFile5.bufnum,\speed, (1.3+(-0.6*((sinFunc-0.7)*sinFunc))) + ((2*rampFunc*0.6*coeff)/durx1), \start, tmx5+(-2*sinFunc)+(-2*cosFunc),\freq, 1000,\q, 1,\frqEnvIdx, 1, \len, (0.22*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.7*sinFunc)+(-0.6*cosFunc),\vol, 1+(0.4*sinFunc*sinFunc)+(0.3*sinFunc) + ((0.1*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp4 = Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\speed, (1.2+(0.5*((cosFunc+0.1)*sinFunc))) - ((2*rampFunc*0.6*coeff)/durx1),\start, tmax6+(-2*sinFunc)+(2*cosFunc),\freq, 1800,\q, 1,\frqEnvIdx, 1, \len, (0.31*coeff),\dur, Pseq([0.3*coeff], durx1/(0.3*coeff)),\pan, (-0.8*sinFunc)+(0.4*cosFunc),\vol, 1-(0.4*(sinFunc+0.2))+(-0.5*sinFunc*cosFunc) + ((0.1*rampFunc*0.6*coeff)/durx1),\envIdx, 1);
			cxp5 = Pbind(\instrument, \instFunc,\bufNum, soundFile2.bufnum,\speed, (1.3+(-0.7*((sinFunc+1.2)*cosFunc))) + ((1*rampFunc*0.2*coeff)/durx1),\start, tmx2 +(-3*sinFunc)+(1*cosFunc),\freq, 1500,\q, 1,\frqEnvIdx, 1, \len, (0.21*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (-0.8*sinFunc)+(-0.9*cosFunc),\vol, 1+(-0.4*(cosFunc+0.1)*cosFunc)+(-0.5*sinFunc) + ((0.1*rampFunc*0.2*coeff)/durx1),\envIdx, 1);
			cxp6 = Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum, \speed, (0.9+(0.8*((cosFunc-0.3)*cosFunc))) - ((1*rampFunc*0.2*coeff)/durx1), \start, tmax4 -(2*sinFunc)+(2*cosFunc),\freq, 1700,\q, 1,\frqEnvIdx, 1, \len, (0.26*coeff),\dur, Pseq([0.2*coeff], durx1/(0.2*coeff)),\pan, (0.7*sinFunc)+(-0.8*cosFunc),\vol, 1+(-0.3*cosFunc*sinFunc)+(0.4*sinFunc) + ((0.1*rampFunc*0.2*coeff)/durx1),\envIdx, 1);

			// reprise duree durx1 avec addition de mat�riel � vitesse deux
			cxp1a = Pbind(\instrument, \instFunc,\bufNum, soundFile1.bufnum, \speed,((1.3-(0.5*(((2*sinFunc)-0.3)*cosFunc))) + ((1*rampFunc*0.4*coeff2)/durx1)), \start, (tmx1 +(2*sinFunc)+(-2*cosFunc)),\freq, 1600,\q, 1,\frqEnvIdx, 1, \len, (0.42*coeff2),\dur, Pseq([0.4*coeff2], durx1/(0.4*coeff2)),\pan, (0.9*sinFunc)+(-0.8*cosFunc),\vol, 1+(-0.4*(cosFunc+0.4)*sinFunc)+(0.5*(sinFunc+0.2)*cosFunc) - ((0.1*rampFunc*0.4*coeff2)/durx1),\envIdx, 1);
			cxp2a = Pbind(\instrument, \instFunc,\bufNum, soundFile4.bufnum,\speed, (1.4+(0.7*((sinFunc-0.9)*(1*cosFunc)))) - ((1*rampFunc*0.2*coeff2)/durx1),\start, tmx4+(-2*sinFunc)+(-2*cosFunc),\freq, 14000,\q, 1,\frqEnvIdx, 1, \len, (0.22*coeff2),\dur, Pseq([0.2*coeff2], durx1/(0.2*coeff2)),\pan, (-0.9*sinFunc)+(0.8*cosFunc),\vol, 1+(0.45*cosFunc*sinFunc)+(-0.4*sinFunc) + ((0.1*rampFunc*0.2*coeff2)/durx1),\envIdx, 1);
			cxp3a = Pbind(\instrument, \instFunc,\bufNum, soundFile6.bufnum,\speed, (1.3+(-0.6*((sinFunc-0.7)*sinFunc))) + ((2*rampFunc*0.6*coeff2)/durx1), \start, tmx6+(-2*sinFunc)+(-2*cosFunc),\freq, 1000,\q, 1,\frqEnvIdx, 1, \len, (0.22*coeff2),\dur, Pseq([0.2*coeff2], durx1/(0.2*coeff2)),\pan, (0.7*sinFunc)+(-0.6*cosFunc),\vol, 1+(0.4*sinFunc*sinFunc)+(0.3*sinFunc) + ((0.1*rampFunc*0.2*coeff2)/durx1),\envIdx, 1);

			// ring modulation
			cxp7 = Pbind(\instrument, \instKlank,\bufNum, soundFile2.bufnum, \start, tmx2-((l2/4)*cosFunc), \speed, 1.4, \len, 0.15*coeff, \dur, Pseq([(0.2/3)*coeff], durx1/((0.2/3)*coeff)), \pan, -0.8*(sinFunc-cosFunc), \vol, 1+(0.1*sinFunc*sinFunc)+(-0.4*cosFunc) + ((0.2*rampFunc*(0.2/3)*coeff)/durx1), \envIdx, 3);
			cxp8 = Pbind(\instrument, \instKlank,\bufNum, soundFile4.bufnum, \start, tmax4-((l4/4)*cosFunc), \speed, 1.6, \len, 0.22*coeff, \dur,Pseq([(0.2)*coeff], durx1/((0.2)*coeff)), \pan, -0.8*(sinFunc+cosFunc), \vol, 1+(-0.3*cosFunc*cosFunc)+(0.2*sinFunc) + ((0.2*rampFunc*0.2*coeff)/durx1), \envIdx, 3);
			cxp9 = Pbind(\instrument, \instKlank,\bufNum, soundFile3.bufnum, \start, tmx3-((l3/4)*sinFunc), \speed, 1.8, \len, 0.23*coeff, \dur,Pseq([0.2*coeff], durx1/(0.2*coeff)), \pan, 0.7*(cosFunc-sinFunc), \vol, 1+(-0.3*sinFunc*cosFunc)+(-0.3*sinFunc) - ((10*rampFunc*0.2*coeff)/durx1), \envIdx, 3);

			// ring modulation vitesse 2
			cxp7a = Pbind(\instrument, \instKlank,\bufNum, soundFile1.bufnum, \start, tmx1-((l2/4)*cosFunc), \speed, 1.4, \len, 0.15*coeff2, \dur, Pseq([(0.2/3)*coeff2], durx1/((0.2/3)*coeff2)), \pan, -0.8*(sinFunc-cosFunc), \vol, 1+(0.1*sinFunc*sinFunc)+(-0.4*cosFunc) + ((0.2*rampFunc*(0.2/3)*coeff2)/durx1), \envIdx, 3);
			cxp8a = Pbind(\instrument, \instKlank,\bufNum, soundFile5.bufnum, \start, tmax5-((l4/4)*cosFunc), \speed, 1.6, \len, 0.22*coeff2, \dur,Pseq([(0.2)*coeff2], durx1/((0.2)*coeff2)), \pan, -0.8*(sinFunc+cosFunc), \vol, 1+(-0.3*cosFunc*cosFunc)+(0.2*sinFunc) + ((0.2*rampFunc*0.2*coeff2)/durx1), \envIdx, 3);
			cxp9a = Pbind(\instrument, \instKlank,\bufNum, soundFile6.bufnum, \start, tmx6-((l3/4)*sinFunc), \speed, 1.8, \len, 0.23*coeff2, \dur,Pseq([0.2*coeff], durx1/(0.2*coeff2)), \pan, 0.7*(cosFunc-sinFunc), \vol, 1+(-0.3*sinFunc*cosFunc)+(-0.3*sinFunc) - ((10*rampFunc*0.2*coeff2)/durx1), \envIdx, 3);

			// desinence  duree durx2
			cxp1d = Pbind(\instrument, \instFunc,\bufNum, Pxrand([soundFile6.bufnum, soundFile3.bufnum, soundFile2.bufnum, soundFile4.bufnum, soundFile1.bufnum, soundFile6.bufnum], inf),\start,  (rrand(min(tmax3, tmx4), min(tmax2, tmx6), min(tmax5, tmx2), min(tmax6, tmx3))) + (0.8*sinFunc)+(0.6*cosFunc),\speed, 1.2-(cosFunc*(cosFunc+0.5)), \freq, 350*(1+ 60*sinFunc),\q, 2,\len, (0.11*coeff),\dur,  Pseq([0.1*coeff], durx2/(0.1*coeff)), \pan, (0.8*sinFunc)+(0.9*cosFunc),\vol,  1-((10*rampFunc*0.1*coeff)/durx2),\envIdx, 1);
			cxp2d = Pbind(\instrument, \instFunc,\bufNum, Pxrand([soundFile2.bufnum, soundFile4.bufnum, soundFile3.bufnum, soundFile1.bufnum, soundFile6.bufnum, soundFile5.bufnum], inf),\start,  (rrand(min(tmx3, tmax4), min(tmx4, tmax1), min(tmax2, tmx5), min(tmax3, tmx6))) + (-0.6*sinFunc)+(-0.6*cosFunc),\speed, 1.3-((sinFunc-0.4)*(cosFunc+0.2)), \freq, 300*(1+ 60*sinFunc),\q, 1.5,\len, (0.16*coeff),\dur,  Pseq([0.15*coeff], durx2/(0.15*coeff)), \pan, (-0.9*sinFunc)+(0.8*cosFunc),\vol,  1-((10*rampFunc*0.15*coeff)/durx2),\envIdx, 1);
			cxp3d = Pbind(\instrument, \instFunc,\bufNum, Pxrand([soundFile3.bufnum, soundFile1.bufnum, soundFile2.bufnum, soundFile4.bufnum, soundFile6.bufnum, soundFile5.bufnum], inf),\start,  (rrand(min(tmax3, tmx2), min(tmax4, tmx1), min(tmx4, tmax5), min(tmx3, tmax2))) + (0.4*sinFunc)+(-0.6*cosFunc),\speed, 1.5-((sinFunc-0.6)*(cosFunc-0.2)), \freq, 300*(1+ 60*sinFunc),\q, 1.5,\len, (0.16*coeff),\dur,  Pseq([0.15*coeff], durx2/(0.15*coeff)), \pan, (0.9*sinFunc)+(0.8*cosFunc),\vol,  1-((10*rampFunc*0.15*coeff)/durx2),\envIdx, 1);


			Ptpar([0, cxp11, 0, cxp12, 0, cxp13, 0, cxp14, 0, cxp15, 0, cxp16,
				(2*pulsz), cxp1, (2*pulsz), cxp2, (2*pulsz), cxp3, (2*pulsz), cxp4, (2*pulsz), cxp5, (2*pulsz), cxp6, (2*pulsz), cxp7, (2*pulsz), cxp8, (2*pulsz), cxp9, (2*pulsz), cxp1a, (2*pulsz), cxp2a, (2*pulsz), cxp3a, cxp7a, (2*pulsz), cxp8a, (2*pulsz), cxp9a, (2*pulsz),
				((2*pulsz)+durx1), cxp1d, ((2*pulsz)+durx1), cxp2d, ((2*pulsz)+durx1), cxp1d   ], 1);
		};

		//-----Partitur de voix 8 du canon 4
		Ptpar([
			czeita8, cella8.value,
			czeitb8, cellb8.value,
			czeitc8, cellc8.value,
			czeitd8, celld8.value,
			czeite8, celle8.value,
			czeitf8, cellf8.value,
			czeitg8, cellg8.value
			], 1);


	};

	//-----Partitur du canon 4
	Ptpar([
		v1zeit, voix1.value,
		v2zeit, voix2.value,
		v3zeit, voix3.value,
		v4zeit, voix4.value,
		v5zeit, voix5.value,
		v6zeit, voix6.value,
		z1zeit, zitat.value,
		v8zeit, voix8.value
		], 1);

};

// -----------------------------------
//------------- Conclusion ------------
// -----------------------------------
conclu= {
	var cxp1, cxp2, cxp3, cxp4, cxp5, cxp6, durx1, durx2, lhx, coeff;

	lhx = (lh6/10); coeff = min((l2/l6), (l5/l6));
	durx1 = min(((l1+l2+l3+l4+l5+l6)/(3/2)), 70); durx2 =0.4;

	cxp1 = Pbind(\instrument, \instRLPF, \filtFrq, 1000 +(300*squared(0.9*sinFunc))-(500*cosFunc), \filtQ, 0.6, \bufNum, soundFile6.bufnum, \start, tmin6 + (0.09*sinFunc)-(0.10*cosFunc), \speed, 3.5+(-0.03*sinFunc)+(0.04*cosFunc),\len, (1.01-(cos(compFunc1)))*(coeff/3), \dur, Pseq([0.03*coeff], durx1/(0.03*coeff)), \pan, (-0.5+((7*rampFunc*0.03*coeff)/durx1)-compFunc1), \vol, (0.5-(min(0.3, vmin6/3)))+(-0.1*sinFunc)+(0.1*cosFunc), \envIdx, 1);
	cxp2 = Pbind(\instrument, \instRLPF, \filtFrq, 1500 +(800*squared(0.7*sinFunc))-(200*cosFunc), \filtQ, 0.6, \bufNum, soundFile4.bufnum, \start, tmin4 , \speed, 4.5-((48*rampFunc*0.03*coeff)/durx2),\len, 0.02*coeff, \dur, Pseq([0.03*coeff], durx2/(0.03*coeff)), \pan, (-1+((20*rampFunc*0.03*coeff)/durx2)), \vol, ((0.7-(min(0.3, vmin4/3)))*(1-((9*rampFunc*0.03*coeff)/durx2))), \envIdx, 1);
	cxp3 = Pbind(\instrument, \instRLPF, \filtFrq, 1500 +(800*squared(0.7*sinFunc))-(200*cosFunc), \filtQ, 0.6, \bufNum, soundFile4.bufnum, \start, tmin4 , \speed, 1.5+((34*rampFunc*0.03*coeff)/durx2),\len, 0.02*coeff, \dur, Pseq([0.03*coeff], durx2/(0.03*coeff)), \pan, (1-((20*rampFunc*0.03*coeff)/durx2)), \vol, ((0.7-(min(0.3, vmin4/3)))*(1-((9*rampFunc*0.03*coeff)/durx2))), \envIdx, 1);

	Ptpar([0.00, cxp1, durx1, cxp2, durx1, cxp3 ], 1);
};

// -------------------------------------------
//-------------  PARTITUR Teil IV--------------
// -------------------------------------------
Ptpar([
	t4zeit1, t4c1p1, t4zeit2, t4c1p2,
	t4zeit3, t4c2p1, t4zeit4, t4c2p2, t4zeit5, t4c2p3,
	t4zeit4, t4c3p1,t4zeit5, t4c3p2,
	t4zeit1 + t4dur, t4c1p1d, t4zeit2+ t4dur, t4c1p2d,
	t4zeit3+ t4dur, t4c2p1d, t4zeit4+ t4dur, t4c2p2d, t4zeit5+ t4dur, t4c2p3d,
	t4zeit4+ t4dur, t4c3p1d,t4zeit5+ t4dur, t4c3p2d,
	canzeit1, canon1.value,
	canzeit2, canon2.value,
	canzeit3, canon3.value,
	canzeit4, canon4.value,
	conzeit, conclu.value
	], 1);

}; // end of Part 4



//**************************************************************************************************
//******************************************         MAIN PROGRAM       ****************************
//**************************************************************************************************

//--- Main Programm --------------------------------------------------------------------------------

//--- Build new window -------------------
w = Window.new("SOLILOQUE SUR [X, X, X ET X]", Rect.new(150, 150, 1050, 535), false);

//--- Build samples ------------------
// Display notice: Application name should remain Soliloque
"WARNING: Application name should always be 'Soliloque', otherwise you may encounter read/write error for your WAV files.".postln;

// Get path for sounds folder
soundsPath = thisProcess.nowExecutingPath.dirname +/+ "sounds";
// add a box for each sample
	sampleView1 = SampleView.new(w, 20,  20, "Sample 1",  soundsPath +/+ "Sample1.wav");
	sampleView2 = SampleView.new(w, 20,  180, "Sample 2", soundsPath +/+ "Sample2.wav");
sampleView3 = SampleView.new(w, 20,  340, "Sample 3",     soundsPath +/+ "Sample3.wav");
	sampleView4 = SampleView.new(w, 540,  20, "Sample 4", soundsPath +/+ "Sample4.wav");
sampleView5 = SampleView.new(w, 540,  180, "Sample 5",    soundsPath +/+ "Sample5.wav");
	sampleView6 = SampleView.new(w, 540,  340, "Sample 6",soundsPath +/+ "Sample6.wav");

("SOLILOQUE: SampleView (library) version " ++ SampleView.version).postln;

// Extra text on the main window
StaticText( w, Rect.new(20 , 470 , 177, 19)).string = "Composition by Fabien Levy";
StaticText( w, Rect.new(20 , 485 , 480, 19)).string = "Programming by Thomas Seelig and Frederic Roskam (c) 2002-2018";
StaticText( w, Rect.new(20 , 500 , 410, 19)).string = "Special thanks to Thomas Noll (Vuza canons) ";

// Number of channels
channelNumberBox = NumberBox(w, Rect(590, 501, 40, 18));
channelNumberBox.value = numChnls;
	channelNumberBox.align = \right;
	channelNumberBox.clipLo = 1.asInteger;
channelNumberBox.clipHi = s.options.numOutputBusChannels;
channelNumberBox.action = {arg numb; ("SOLILOQUE: The number of channels is now " ++ numb.value).postln; numChnls = numb.value.asInteger;};
StaticText( w, Rect.new(635, 500, 80, 19)).string = "/ " ++ s.options.numOutputBusChannels ++ " channels";

// Go button
theGoButton = Button( w, Rect.new(750, 480, 130, 40), "START");
theGoButton.states = [  ["START", Color.black, Color.new255(177,237,105)]];
// Go button action
theGoButton.action = {| view |
	"SOLILOQUE: Go".postln;

	//--- SoundFiles -------------------------
	soundFile1 = sampleView1.buffer;
	soundFile2 = sampleView2.buffer;
	soundFile3 = sampleView3.buffer;
	soundFile4 = sampleView4.buffer;
	soundFile5 = sampleView5.buffer;
	soundFile6 = sampleView6.buffer;

	//--- Steuerparameter -------------------
	tmax1 = sampleView1.maxpos1 / sampleRate;
	tmx1  = sampleView1.maxpos2 / sampleRate;
	tmin1 = sampleView1.minpos / sampleRate;
	fmax1 = sampleView1.maxfreq1;
	fmx1  = sampleView1.maxfreq2;
	fmin1 = sampleView1.minfreq;
	vmax1 = sampleView1.maxamp1;
	vmx1  = sampleView1.maxamp2;
	vmin1 = sampleView1.minamp;
	l1  = sampleView1.len;

	tmax2 = sampleView2.maxpos1 / sampleRate;
	tmx2  = sampleView2.maxpos2 / sampleRate;
	tmin2 = sampleView2.minpos / sampleRate;
	fmax2 = sampleView2.maxfreq1;
	fmx2  = sampleView2.maxfreq2;
	fmin2 = sampleView2.minfreq;
	vmax2 = sampleView2.maxamp1;
	vmx2  = sampleView2.maxamp2;
	vmin2 = sampleView2.minamp;
	l2  = sampleView2.len;

	tmax3 = sampleView3.maxpos1 / sampleRate;
	tmx3  = sampleView3.maxpos2 / sampleRate;
	tmin3 = sampleView3.minpos / sampleRate;
	fmax3 = sampleView3.maxfreq1;
	fmx3  = sampleView3.maxfreq2;
	fmin3 = sampleView3.minfreq;
	vmax3 = sampleView3.maxamp1;
	vmx3  = sampleView3.maxamp2;
	vmin3 = sampleView3.minamp;
	l3  = sampleView3.len;

	tmax4 = sampleView4.maxpos1 / sampleRate;
	tmx4  = sampleView4.maxpos2 / sampleRate;
	tmin4 = sampleView4.minpos / sampleRate;
	fmax4 = sampleView4.maxfreq1;
	fmx4  = sampleView4.maxfreq2;
	fmin4 = sampleView4.minfreq;
	vmax4 = sampleView4.maxamp1;
	vmx4  = sampleView4.maxamp2;
	vmin4 = sampleView4.minamp;
	l4  = sampleView4.len;

	tmax5 = sampleView5.maxpos1 / sampleRate;
	tmx5  = sampleView5.maxpos2 / sampleRate;
	tmin5 = sampleView5.minpos / sampleRate;
	fmax5 = sampleView5.maxfreq1;
	fmx5  = sampleView5.maxfreq2;
	fmin5 = sampleView5.minfreq;
	vmax5 = sampleView5.maxamp1;
	vmx5  = sampleView5.maxamp2;
	vmin5 = sampleView5.minamp;
	l5  = sampleView5.len;

	tmax6 = sampleView6.maxpos1 / sampleRate;
	tmx6  = sampleView6.maxpos2 / sampleRate;
	tmin6 = sampleView6.minpos / sampleRate;
	fmax6 = sampleView6.maxfreq1;
	fmx6  = sampleView6.maxfreq2;
	fmin6 = sampleView6.minfreq;
	vmax6 = sampleView6.maxamp1;
	vmx6  = sampleView6.maxamp2;
	vmin6 = sampleView6.minamp;
	l6  = sampleView6.len;

	//--------------------------------III.10 Valeurs de temps ------------------------------
	lh1 = max(((l1 - l1.floor)*10), 0.5);
	lh2 = max(((l2 - l2.floor)*10), 0.4);
	lh3 = max(((l3 - l3.floor)*10), 0.3);
	lh4 = max(((l4 - l4.floor)*10), 0.6);
	lh5 = max(((l5 - l5.floor)*10), 0.7);
	lh6 = max(((l6 - l6.floor)*10), 0.8);  // valeur entre 0.3 et 10

	minmax1 = min((l1-tmax1), tmax1); //******** ATTENTION NOUVEAUX CALCULS de valeurs
	minmax2 = min((l2-tmax2), tmax1);
	minmax3 = min((l3-tmax3), tmax1);
	minmax4 = min((l4-tmax4), tmax1);
	minmax5 = min((l5-tmax5), tmax1);
	minmax6 = min((l6-tmax6), tmax1);

	minmx1 = min((l1-tmx1), tmx1);
	minmx2 = min((l2-tmx2), tmx1);
	minmx3 = min((l3-tmx3), tmx1);
	minmx4 = min((l4-tmx4), tmx1);
	minmx5 = min((l5-tmx5), tmx1);
	minmx6 = min((l6-tmx6), tmx1);

	minmin1 = min((l1-tmin1), tmin1);
	minmin2 = min((l2-tmin2), tmin1);
	minmin3 = min((l3-tmin3), tmin1);
	minmin4 = min((l4-tmin4), tmin1);
	minmin5 = min((l5-tmin5), tmin1);
	minmin6 = min((l6-tmin6), tmin1);

	postf("Number of channels %\n",numChnls);

	//=======================SynthDefs==============================

	// Note: the SynthDef can only be defined now because they depend on the number of output channels

	// SynthDef #1
	SynthDef (\instFunc,
	{
		arg i_out = 0,
		bufNum,       // ATTENTION : MANQUE LA VALEUR PAR DEFAUT !
		pan= 0,       // Panorama
		vol = 1,      // Volume 0...1
		len = 1,      // Length 1 = soundfilelength
		start = 0,    // Depart du jeu 0...1 (= End)
		envIdx = 0,   // Envelope NEW
		speed = 1;    // Speed 1 = Original

		var out,envelopes,env;

		// limit volume to [-1;1]
		vol = vol.clip(-1,1);

		envelopes = [
		Env.new(#[1, 1], #[1.0]).asArray,                   //rect
		Env.new(#[1, 0], #[1.0]).asArray,                   //saw DOWN
		Env.new(#[0, 1, 0], #[1.0, 0.0]).asArray,           // saw UP
		Env.new(#[0, 1, 0], #[0.5, 0.5]).asArray,           // triangle
		Env.new(#[0, 1, 1, 0], #[0.01, 1.0, 0.01]).asArray, //recti
		Env.new(#[0, 1, 0], #[0.01, 1.0]).asArray,          //saw DOWN i
		Env.new(#[0, 1, 0], #[1.0, 0.01]).asArray,          //saw UP i
		Env.new([0.0001, 1, 1],[0.2, 10],'exponential').asArray //fade IN
		];
		env = Select.kr(envIdx,envelopes);

		out = PanAz.ar(
			numChnls,
			EnvGen.ar(
				env,
				gate:1.0,   // loop
				timeScale: len,
				doneAction: 2
				)*PlayBuf.ar(
				1,    // channels
				bufNum, // bufnum
				BufRateScale.kr(bufNum)*speed,    // rate
				0,    // trigger
				BufRateScale.kr(bufNum)*(Server.default.sampleRate)*start, // startpos
				0   // no loop
				),
				pan
				);

		Out.ar(i_out, vol*out);
		}).store;

	// SynthDef #2: with resonant low pass filter
	SynthDef(\instFiltr, {
		arg  i_out = 0,
		bufNum,      //
		pan = 0,      // Panoramique
		vol = 1,     // Volume 0...1
		len = 1,     // length 0...1
		start = 0,   // Depart 0...1
		envIdx,      // Envelope index
		speed = 1,   // Speed 1 = Original
		freq = 1000,   // frequence of the Filter
		q = 0.3,     // Resonanzfaktor
		frqEnvIdx;   // Frequency envelope index

		var  out;  // Length in Samples
		var  envelopes,env,frqenv;

		// limit volume to [-1;1]
		vol = vol.clip(-1,1);

		envelopes = [
		Env.new(#[1, 1], #[1.0]).asArray,                   // rect
		Env.new(#[1, 0], #[1.0]).asArray,                   // saw DOWN
		Env.new(#[0, 1, 0], #[1.0, 0.0]).asArray,           // saw UP
		Env.new(#[0, 1, 0], #[0.5, 0.5]).asArray,           // triangle
		Env.new(#[0, 1, 1, 0], #[0.01, 1.0, 0.01]).asArray, // recti
		Env.new(#[0, 1, 0], #[0.01, 1.0]).asArray,          // saw DOWN i
		Env.new(#[0, 1, 0], #[1.0, 0.01]).asArray,          // saw UP i
		Env.new([0.0001, 1, 1],[0.2, 10],'exponential').asArray //fade IN
		];
		env = Select.kr(envIdx,envelopes);
		frqenv = Select.kr(frqEnvIdx,envelopes);

		// Multichannel equal power panner
		out = PanAz.ar(
			// number of output channels
			numChnls,
			EnvGen.ar(
				env,
				gate:1.0,    // loop
				timeScale: len,
				doneAction: 2
				)*RLPF.ar(
				// in signal:
				PlayBuf.ar(
					1,   // channels
					bufNum,  // bufnum
					BufRateScale.kr(bufNum)*speed, // rate
					0,   // trigger
					BufRateScale.kr(bufNum)*(Server.default.sampleRate)*start,  // startpos
					0    // no loop
					),   // loop, PlayBuf
				EnvGen.kr(
					frqenv,
					levelScale: freq,
					timeScale: len
					),
				// rq: the reciprocal of Q.  bandwidth / cutoffFreq, between 0 and 1
				q
				),
				// pan position:
				pan
				);

		// write to the output bus
		Out.ar(i_out, vol*out);
		}).store;  // instFunc


	// SynthDef #3: with second order low pass filter
	SynthDef(\instFltrpb, {
		arg  i_out = 0,
		bufNum,      //
		pan= 0,      // Panoramique
		vol = 1,     // Volume 0...1
		len = 1,     // length 0...1
		start = 0,   // Depart 0...1
		envIdx,      // Envelope index
		speed = 1,   // Speed 1 = Original
		freq = 1000,   // frequence of the Filter
		q = 0.3,     // Resonanzfaktor
		frqEnvIdx;   // Frequency envelope index

		var  out;  // Length in Samples
		var  envelopes,env,frqenv;

		// limit volume to [-1;1]
		vol = vol.clip(-1,1);

		envelopes = [
		Env.new(#[1, 1], #[1.0]).asArray,                   // rect
		Env.new(#[1, 0], #[1.0]).asArray,                   // saw DOWN
		Env.new(#[0, 1, 0], #[1.0, 0.0]).asArray,           // saw UP
		Env.new(#[0, 1, 0], #[0.5, 0.5]).asArray,           // triangle
		Env.new(#[0, 1, 1, 0], #[0.01, 1.0, 0.01]).asArray, // recti
		Env.new(#[0, 1, 0], #[0.01, 1.0]).asArray,          // saw DOWN i
		Env.new(#[0, 1, 0], #[1.0, 0.01]).asArray,          // saw UP i
		Env.new([0.0001, 1, 1],[0.2, 10],'exponential').asArray // fade IN
		];
		env = Select.kr(envIdx,envelopes);
		frqenv = Select.kr(frqEnvIdx,envelopes);

		// Multichannel equal power panner
		out = PanAz.ar(
			// number of output channels
			numChnls,
			EnvGen.ar(
				env,
				gate:1.0,    // loop
				timeScale: len,
				doneAction: 2
				)*BPF.ar(// 2nd order Butterworth bandpass filter
				// in signal:
				PlayBuf.ar(
					1,   // channels
					bufNum,  // bufnum
					BufRateScale.kr(bufNum)*speed, // rate
					0,   // trigger
					BufRateScale.kr(bufNum)*(Server.default.sampleRate)*start,  // startpos
					0    // no loop
					),   // loop, PlayBuf
				EnvGen.kr(
					frqenv,
					levelScale: freq,
					timeScale: len
					),
				// rq: the reciprocal of Q.  bandwidth / cutoffFreq, between 0 and 1
				q
				),
				// pan position:
				pan
				);

		Out.ar(i_out, vol*out);
		}).store;  // instFunc


	// SynthDef #4: Resonant low pass filter
	SynthDef(\instRLPF, {
		arg  i_out,
		bufNum,      //
		pan= 0,      // Panoramique
		vol = 1,     // Volume 0...1
		len = 1,     // length 0...1
		start = 0,   // Depart 0...1
		envIdx,      // Envelope index
		speed = 1,   // Speed 1 = Original
		filtFrq = 1000,    // frequence of the Filter
		filtQ = 0.3;     // Resonanzfaktor

		var  out;           // Length in Samples
		var  envelopes,env; // Length in Samples

		// DEBUG
		//SendTrig.kr(Impulse.kr(1), 0, filtFrq);

		// limit volume to [-1;1]
		vol = vol.clip(-1,1);

		envelopes = [
		Env.new(#[1, 1], #[1.0]).asArray,                   // rect
		Env.new(#[1, 0], #[1.0]).asArray,                   // saw DOWN
		Env.new(#[0, 1, 0], #[1.0, 0.0]).asArray,           // saw UP
		Env.new(#[0, 1, 0], #[0.5, 0.5]).asArray,           // triangle
		Env.new(#[0, 1, 1, 0], #[0.01, 1.0, 0.01]).asArray, // recti
		Env.new(#[0, 1, 0], #[0.01, 1.0]).asArray,          // saw DOWN i
		Env.new(#[0, 1, 0], #[1.0, 0.01]).asArray,          // saw UP i
		Env.new([0.0001, 1, 1],[0.2, 10],'exponential').asArray // fade IN
		];
		env = Select.kr(envIdx,envelopes);

		out = PanAz.ar(
			// number of output channels
			numChnls,
			EnvGen.ar(
				env,
				gate:1.0,    // loop
				timeScale: len,
				doneAction: 2
				)*RLPF.ar(// 2nd order Butterworth bandpass filter
				// in signal:
				PlayBuf.ar(
					1,   // channels
					bufNum,  // bufnum
					BufRateScale.kr(bufNum)*speed, // rate
					0,   // trigger
					BufRateScale.kr(bufNum)*(Server.default.sampleRate)*start,  // startpos
					0    // no loop
					),   // loop, PlayBuf
				// cutoff frequency
				filtFrq,
				// rq: the reciprocal of Q.  bandwidth / cutoffFreq, between 0 and 1
				filtQ
				),
				// pan position:
				pan
				);


		Out.ar(i_out, vol*out);
		}).store;  /* \instRLPF Resonant Low Pass filter*/


	// SynthDef #5: Bank of random frequency resonators
	SynthDef(\instKlank, {
		arg  i_out,
		bufNum,
		speed = 1,
		vol = 1,
		start = 0, // 0...1
		envIdx = 0,
		len = 5,
		pan = 0;

		var envelopes, out,env;

		// limit volume to [-1;1]
		vol = vol.clip(-1,1);

		envelopes = [
		Env.new(#[1, 1], #[1.0]).asArray,                   // rect
		Env.new(#[1, 0], #[1.0]).asArray,                   // saw DOWN
		Env.new(#[0, 1, 0], #[1.0, 0.0]).asArray,           // saw UP
		Env.new(#[0, 1, 0], #[0.5, 0.5]).asArray,           // triangle
		Env.new(#[0, 1, 1, 0], #[0.01, 1.0, 0.01]).asArray, // recti
		Env.new(#[0, 1, 0], #[0.01, 1.0]).asArray,          // saw DOWN i
		Env.new(#[0, 1, 0], #[1.0, 0.01]).asArray,          // saw UP i
		Env.new([0.0001, 1, 1],[0.2, 10],'exponential').asArray // fade IN
		];
		env = Select.kr(envIdx,envelopes);

		out = PanAz.ar(
			numChnls,
			EnvGen.ar(
				env,
				gate:1.0,    // loop
				timeScale:len,
				doneAction:2
				)*
			// Limits the input amplitude to the given level
			Limiter.ar(
				// Klank is a bank of fixed frequency resonators which can be used
				// to simulate the resonant modes of an object
				Klank.ar(
					// 12 frequencies between 200 and 4000 Hz
					`[Array.rand(12, 200, 4000), // frequencies
					nil,             // amplitudes (nil: default to 1.0)
					// 12 amplitudes between 0.5 and 8 s
					Array.rand(12, 0.5, 8)],   // ring times
					// input:
					PlayBuf.ar(
						1,   // channels
						bufNum,  // bufnum
						BufRateScale.kr(bufNum)*speed,// rate
						0,   // trigger
						BufRateScale.kr(bufNum)*(Server.default.sampleRate)*start,  // startpos
						0    // no loop
						)
					),
				level:0.25,
				dur:0.02
				),
			pan
			);

		Out.ar(i_out, vol*out);
		}).store;  /* \instKlank Ring modulator*/


	// SynthDef #6: Bank of fixed frequency resonators
	SynthDef(\instKlankd, {
		arg  i_out,
		bufNum,
		speed = 1,
		vol = 1,
		start = 0, // 0...1
		envIdx = 0,
		len = 5,
		pan = 0,
		// vecteur de definition des filtres :
		// 4 frequences, 4 amplitudes (nil = 0), 5 temporalites en seconde
		specf = #[800, 1071, 1153, 1723], speca = #[1, 1, 1, 1], spect = #[1, 1, 1, 1];

		var envelopes, out,env;

		// limit volume to [-1;1]
		vol = vol.clip(-1,1);

		envelopes = [
		Env.new(#[1, 1], #[1.0]).asArray,      //rect
		Env.new(#[1, 0], #[1.0]).asArray,      //saw DOWN
		Env.new(#[0, 1, 0], #[1.0, 0.0]).asArray,    // saw UP
		Env.new(#[0, 1, 0], #[0.5, 0.5]).asArray,    // triangle
		Env.new(#[0, 1, 1, 0], #[0.01, 1.0, 0.01]).asArray, //recti
		Env.new(#[0, 1, 0], #[0.01, 1.0]).asArray, //saw DOWN i
		Env.new(#[0, 1, 0], #[1.0, 0.01]).asArray, //saw UP i
		Env.new([0.0001, 1, 1],[0.2, 10],'exponential').asArray //fade IN
		];
		env = Select.kr(envIdx,envelopes);

		out = PanAz.ar(
			numChnls,
			EnvGen.ar(
				env,
				gate:1.0,    // loop
				timeScale:len,
				doneAction:2
				)*
			// Limits the input amplitude to the given level
			Limiter.ar(
				// Klank is a bank of fixed frequency resonators which can be used
				// to simulate the resonant modes of an object
				Klank.ar(
					// 12 frequencies between 200 and 4000 Hz
					`[Array.rand(12, 200, 4000), // frequencies
					nil,             // amplitudes (nil: default to 1.0)
					// 12 amplitudes between 0.5 and 8 s
					Array.rand(12, 0.5, 8)],   // ring times
					// input:
					PlayBuf.ar(
						1,   // channels
						bufNum,  // bufnum
						BufRateScale.kr(bufNum)*speed,// rate
						0,   // trigger
						BufRateScale.kr(bufNum)*(Server.default.sampleRate)*start,  // startpos
						0    // no loop
						)
					),
				level:0.25,
				dur:0.02
				),
			pan
			);

		Out.ar(i_out, vol*out);
		}).store;  /* \instKlank Ring modulator*/

	// SynthDef #7: Two channel linear crossfader
	SynthDef(\instXFade, {
		arg  i_out,
		bufNum1,
		bufNum2,
		speed = 1,
		vol = 1,
		start = 0, // 0...1
		envIdx = 0,
		len = 5,
		pan = 0,
		theCrossFreq = 0;

		var envelopes, out,env;

		// limit volume to [-1;1]
		vol = vol.clip(-1,1);

		envelopes = [
		Env.new(#[1, 1], #[1.0]).asArray,      //rect
		Env.new(#[1, 0], #[1.0]).asArray,      //saw DOWN
		Env.new(#[0, 1, 0], #[1.0, 0.0]).asArray,    // saw UP
		Env.new(#[0, 1, 0], #[0.5, 0.5]).asArray,    // triangle
		Env.new(#[0, 1, 1, 0], #[0.01, 1.0, 0.01]).asArray, //recti
		Env.new(#[0, 1, 0], #[0.01, 1.0]).asArray, //saw DOWN i
		Env.new(#[0, 1, 0], #[1.0, 0.01]).asArray, //saw UP i
		Env.new([0.0001, 1, 1],[0.2, 10],'exponential').asArray //fade IN
		];
		env = Select.kr(envIdx,envelopes);

		out = PanAz.ar(
			// number of output channels
			numChnls,
			EnvGen.ar(
				env,
				gate:1.0,    // loop
				timeScale: len,
				doneAction: 2
				)*LinXFade2.ar(
				// in signal:
				PlayBuf.ar(
					1,   // channels
					bufNum1, // bufnum
					BufRateScale.kr(bufNum1)*speed,  // rate
					0,   // trigger
					BufRateScale.kr(bufNum1)*(Server.default.sampleRate)*start, // startpos
					0    // no loop
					),   // loop, PlayBuf,
				PlayBuf.ar(
					1,   // channels
					bufNum2, // bufnum
					BufRateScale.kr(bufNum2)*speed,  // rate
					0,   // trigger
					BufRateScale.kr(bufNum2)*(Server.default.sampleRate)*start, // startpos
					0    // no loop
					),   // loop, PlayBuf
				// triangle oscillator
				LFTri.kr(theCrossFreq/len)
				),
				// pan position:
				pan
				);
		Out.ar(i_out, vol*out);
	}).store;  /* \instXFade */

	~ptpar = Ptpar(
		// (time, pattern)
		[
		timeOffset, teil1.value,
		globalT1, teil2.value,
		globalT2, teil31.value,
		globalT31,  teil32.value,
		globalT32,  teil33.value,
		globalT33,  teil4.value
		],
		// repeat 1 time
		1).play;
	};  // action

	// Stop button
	theStopButton = Button( w, Rect.new(900, 480, 130, 40), "STOP");
	theStopButton.states = [["STOP", Color.white,Color.new255(136,27,20)]];
	theStopButton.action = {| view |
		"SOLILOQUE: Stop".postln;
		~ptpar.stop;
		Server.freeAll;
		durationText.string = "";
	};

	// Move main window to front
	w.front;

}); // END wait for boot

//)
// ^ double-click up here and press RETURN
