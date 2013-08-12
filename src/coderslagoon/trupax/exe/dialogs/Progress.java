/*
Copyright 2010-2013 CODERSLAGOON

This file is part of TruPax.

TruPax is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

TruPax is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
TruPax. If not, see http://www.gnu.org/licenses/.
*/

package coderslagoon.trupax.exe.dialogs;

import java.util.Properties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import coderslagoon.baselib.swt.dialogs.Dialog;
import coderslagoon.baselib.swt.util.SWTUtil;
import coderslagoon.baselib.swt.util.ToolTips;
import coderslagoon.trupax.exe.Exe;
import coderslagoon.trupax.exe.GUI;
import coderslagoon.trupax.exe.NLS;


public class Progress extends Dialog {
    Label       lblObject;
    Label       lblInfo;
    ProgressBar pbar;
    Button      btnCancel;
    String      closeConfirmation;
    boolean     canceled;
    
    public Progress(Shell parent, Properties props, boolean bar, ToolTips toolTips) {
        super(parent, props,
              "progress." + (bar ? "bar" : "nobar"),   
              SWT.DIALOG_TRIM | SWT.RESIZE | SWT.BORDER);
        
        addListener(SWT.Close, new ClosingListener());
        
        GridLayout gl = new GridLayout(1, false);
        setLayout(gl);
        
        this.lblObject = new Label(this, SWT.NONE);
        this.lblObject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        SWTUtil.adjustFontSize(this.lblObject, 1);
        
        if (bar) {
            this.pbar = new ProgressBar(this, SWT.NONE);
            this.pbar.setMinimum(0);
            this.pbar.setMaximum(PROGRESS_MAX);
            this.pbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        }
            
        Composite bottom = new Composite(this, SWT.NONE);
        bottom.setLayoutData(new GridData (SWT.FILL, SWT.FILL, true, true));
        gl = new GridLayout(2, false);
        gl.marginWidth = 0;
        gl.marginHeight = 0;
        bottom.setLayout(gl);

        this.lblInfo = new Label(bottom, SWT.WRAP);
        this.lblInfo.setLayoutData(new GridData (SWT.FILL, SWT.FILL, true, true));

        this.btnCancel = new Button(bottom, SWT.PUSH);
        this.btnCancel.setText(NLS.PROGRESS_BTN_CANCEL.s());  
        this.btnCancel.setLayoutData(new GridData(SWT.BEGINNING, SWT.END, false, false));
        this.btnCancel.addListener(SWT.Selection, this.onCancel);
        this.btnCancel.pack();
        toolTips.add(this.btnCancel, NLS.PROGRESS_TOOLTIP_BTN_CANCEL); 
        SWTUtil.adjustButtonSize(this.btnCancel, GUI.BTN_ADJ_FCT);

        setImage(parent.getImage());
        setMinimumSize(computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
        toolTips.shellListen(this);
    }

    ///////////////////////////////////////////////////////////////////////////

    public void setCaption(String caption) {
    	if (this.isDisposed()) {
    		return;
    	}
        setText(NLS.PROGRESS_CAPTION_2.fmt(Exe.PRODUCT_NAME, caption)); 
    }
    
    public void setInfo(String info) {
    	if (this.isDisposed()) {
    		return;
    	}
        this.lblInfo.setText(info);
    }

    public void setObject(String obj) {
    	if (this.isDisposed()) {
    		return;
    	}
        this.lblObject.setText(obj);
    }

    public void setCloseConfirmation(String text) {
        this.closeConfirmation = text;
    }
    
    public boolean canceled() {
        return this.canceled;
    }
    
    ///////////////////////////////////////////////////////////////////////////

    final static int PROGRESS_MAX = 1000 * 1000;

    public void setProgressMax(long max) {
        this.punit = (double)max / (double)PROGRESS_MAX;
        this.lastPBarSel = -1;
    }
    double punit;

    public void setProgress(long pos) {
    	if (this.isDisposed() || null == this.pbar) {
    		return;
    	}
        double dpos = (double)pos;
        dpos /= this.punit;
        int pbarSel = (int)dpos;
        if (this.lastPBarSel != pbarSel) { // avoids widget thrashing 
        	this.pbar.setSelection(this.lastPBarSel = pbarSel);
        }
    }
    int lastPBarSel = -1;

    ///////////////////////////////////////////////////////////////////////////
    
    Listener onCancel = new Listener() {
        public void handleEvent(Event evt) {
            Progress.this.close();
        }
    };

    ///////////////////////////////////////////////////////////////////////////

    class ClosingListener implements Listener {
        public void handleEvent(Event evt) {
            if (null != Progress.this.closeConfirmation) {
                MessageBox msgBox = new MessageBox(
                        Progress.this,
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                
                msgBox.setText(NLS.PROGRESS_MSGBOX_CONFIRM.s()); 
                msgBox.setMessage(Progress.this.closeConfirmation);
                
                evt.doit = SWT.YES == msgBox.open();
            }
            else {
                evt.doit = true;
            }
            if (evt.doit) {
                Progress.this.canceled = true;
            }
        }
    }
}
