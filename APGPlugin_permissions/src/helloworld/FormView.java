package helloworld;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

public class FormView extends ViewPart {
	public static final String ID = "helloworld.view";
	 private FormToolkit toolkit;
	 private ScrolledForm form;

	 /**
	  * The constructor.
	  */
	 public FormView() {
	 }

	 /**
	  * This is a callback that will allow us to create the viewer and
	  * initialize it.
	  */
	 public void createPartControl(Composite parent) {
	  toolkit = new FormToolkit(parent.getDisplay());
	  form = toolkit.createScrolledForm(parent);
	  form.setText("Hello, Eclipse Forms");
	 }

	 /**
	  * Passing the focus request to the form.
	  */
	 public void setFocus() {
	  form.setFocus();
	 }

	 /**
	  * Disposes the toolkit
	  */
	 public void dispose() {
	  toolkit.dispose();
	  super.dispose();
	 }
	}
