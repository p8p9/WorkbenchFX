package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.WorkbenchModule;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display {@link WorkbenchModule}s as tabs in the toolbar.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Tab extends Control {
  private static final Logger LOGGER = LogManager.getLogger(Tab.class.getName());

  private final Workbench workbench;
  private final ObservableList<WorkbenchModule> modules;

  private final ObjectProperty<WorkbenchModule> module;
  private final StringProperty name;
  private final ObjectProperty<Node> icon;
  private final BooleanProperty activeTab;

  /**
   * Constructs a new {@link Tab}.
   *
   * @param workbench which created this {@link Tab}
   */
  public Tab(Workbench workbench) {
    this.workbench = workbench;
    this.modules = workbench.getModules();
    module = new SimpleObjectProperty<>();
    name = new SimpleStringProperty();
    icon = new SimpleObjectProperty<>();
    activeTab = new SimpleBooleanProperty();
    setupModuleListeners();
    setupActiveTabListener();
  }

  private void setupModuleListeners() {
    module.addListener(observable -> {
      WorkbenchModule current = getModule();
      name.setValue(current.getName());
      icon.setValue(current.getIcon());
    });
  }

  private void setupActiveTabListener() {
    // whenever the module of this tab changes, re-initialize the binding which determines whether
    // this tab is the currently active tab or not
    moduleProperty().addListener(observable -> {
      activeTab.unbind();
      activeTab.bind(Bindings.equal(getModule(), workbench.activeModuleProperty()));
    });
  }

  /**
   * Closes the {@link WorkbenchModule} along with this {@link Tab}.
   */
  public void close() {
    workbench.closeModule(getModule());
  }

  /**
   * Opens the {@link WorkbenchModule} belonging to this {@link Tab}.
   */
  public void open() {
    workbench.openModule(getModule());
  }

  public WorkbenchModule getModule() {
    return module.get();
  }

  /**
   * Defines the {@code module} which is being represented by this {@link Tab}.
   *
   * @param module to be represented by this {@link Tab}
   */
  public final void setModule(WorkbenchModule module) {
    LOGGER.trace("Setting reference to module");
    this.module.set(module);
  }

  public ReadOnlyObjectProperty<WorkbenchModule> moduleProperty() {
    return module;
  }

  public String getName() {
    return name.get();
  }

  public ReadOnlyStringProperty nameProperty() {
    return name;
  }

  public Node getIcon() {
    return icon.get();
  }

  public ReadOnlyObjectProperty<Node> iconProperty() {
    return icon;
  }

  public boolean isActiveTab() {
    return activeTab.get();
  }

  public ReadOnlyBooleanProperty activeTabProperty() {
    return activeTab;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new TabSkin(this);
  }
}