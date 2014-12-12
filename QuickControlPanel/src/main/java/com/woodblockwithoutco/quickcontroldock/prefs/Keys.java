/*******************************************************************************
 * Copyright 2014 Alexander Leontyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.woodblockwithoutco.quickcontroldock.prefs;

public final class Keys {
	
	public static final class Color {
		public static final String IDLE_COLOR = "colors_idle_color"; 
		public static final String ACTIVE_COLOR = "colors_active_color"; 
		public static final String PRESSED_COLOR = "colors_pressed_color"; 
		public static final String PANEL_BACKGROUND_COLOR = "colors_panel_background_color"; 
		public static final String PANEL_SECOND_BACKGROUND_COLOR = "colors_panel_background_color_2"; 
		public static final String PANEL_GRADIENT_DIRECTION = "colors_gradient_direction"; 
		public static final String PANEL_SECTION_BG_COLOR = "colors_section_background_color"; 
		public static final String PANEL_SECTION_SHADOW_COLOR = "colors_section_shadow_color"; 
		public static final String SEEKBAR_PROGRESS_COLOR = "colors_seekbar_progress_color"; 
		public static final String SEEKBAR_THUMB_COLOR = "colors_seekbar_thumb_color"; 
		public static final String SEEKBAR_THUMB_PROGRESS_SAME_COLOR = "colors_seekbar_thumb_same_color"; 
		public static final String TEXT_COLOR = "colors_text"; 
		public static final String TOGGLE_BG_COLOR = "colors_toggle_bg_color"; 
		public static final String DISABLE_SEEKBAR_COLORING = "colors_disable_seekbar_coloring"; 
		public static final String SEEKBAR_ICON_COLOR_ACTIVE = "colors_seekbar_icon_same_as_active_color"; 
		public static final String SEEKBAR_ICON_COLOR = "colors_seekbar_icon_color"; 
	}
	
	public static final class Launch {
		public static final String SWIPE_DETECTOR_SIZE_1 = "dimens_swipe_detector_size_1"; 
		public static final String SWIPE_DETECTOR_SIZE_2 = "dimens_swipe_detector_size_2"; 
		public static final String SWIPE_DETECTOR_ALIGN = "dimens_swipe_detector_align"; 
		public static final String SWIPE_DETECTOR_DIRECTION = "dimens_swipe_detector_direction"; 
		public static final String SWIPE_DETECTOR_ENABLED = "swipe_detector_enabled"; 
		public static final String SWIPE_DETECTOR_VIBRATE = "swipe_detector_vibrate";  
		public static final String SWIPE_DETECTOR_OFFSET = "swipe_detector_offset"; 
		public static final String SWIPE_DETECTOR_LOAD_ON_BOOT = "swipe_detector_load_on_boot"; 
		public static final String SERVICE_ENABLED = "swipe_detector_service_enabled"; 
		public static final String SWIPE_DETECTOR_GOOGLE_NOW_GESTURE_ENABLED = "swipe_detector_gn_gesture_enabled"; 
		public static final String LAUNCH_LAUNCHER_ICON = "launch_launcher_icon"; 
	}
	
	public static final class Toggles {
		public static final String BUTTONS_ENABLED = "toggles_buttons_enabled"; 
		public static final String BUTTONS_USED = "toggles_buttons_used"; 
		public static final String BUTTONS_ORDER = "toggles_buttons_order"; 
		public static final String BORDER_DRAWABLE = "toggles_border_drawable"; 
		public static final String BORDER_CORNER_RADIUS = "toggles_border_corner_radius"; 
		public static final String BACKGROUND_CORNER_RADIUS = "toggles_background_corner_radius"; 
		public static final String BORDER_WIDTH = "toggles_border_width"; 
		public static final String BACKGROUND_DRAWABLE = "toggles_bg_drawable"; 
		public static final String BUTTONS_SIZE = "toggles_buttons_size"; 
		public static final String FLASHLIGHT_TYPE = "toggles_flashlight_type"; 
		public static final String BUTTONS_DISTANCE = "toggles_buttons_distance"; 
		public static final String SOUND_MODES = "toggles_sound_modes"; 
		public static final String SEEKBARS_USED = "toggles_seekbars_used"; 
		public static final String SEEKBARS_ORDER = "toggles_seekbars_order"; 
		public static final String BRIGHTNESS_MIN_VALUE = "toggle_brightness_min_value"; 
		public static final String ENABLE_LONG_CLICK = "toggle_enable_long_click"; 
		public static final String CLOSE_AFTER_CLICK = "toggle_close_after_click"; 
		public static final String SEEKBARS_ON_TOP = "toggles_seekbars_on_top"; 
		public static final String TIMEOUT_MODES = "toggles_timeout_mode"; 
	}
	
	public static final class Info {
		public static final String INFO_ENABLED = "info_enabled"; 
		public static final String DATE_FORMAT = "info_date_format"; 
		public static final String INFO_ITEMS_ENABLED = "info_items_enabled"; 
		public static final String INFO_ITEMS_STRING = "info_items_order"; 
		public static final String INFO_ITEMS_SIZE = "info_items_text_size"; 
	}
	
	public static final class Lockscreen {
		public static final String SWIPE_DETECTOR_ENABLED_ON_LOCKSCREEN = "lockscreen_swipe_detector_enabled";  
	}
	
	public static final class Music {
		public static final String ENABLE_TITLE = "music_enable_title"; 
		public static final String ENABLE_ALBUM = "music_enable_album"; 
		public static final String ENABLE_ARTIST = "music_enable_artist"; 
		public static final String ENABLE_SCRUBBING = "music_enable_scrubbing"; 
		public static final String BUTTON_SIZE = "music_button_size"; 
		public static final String MUSIC_ENABLED = "music_panel_enabled"; 
		public static final String USE_BROADCAST_EVENT = "music_broadcast_event"; 
		public static final String METADATA_SIZE = "music_metadata_size"; 
		public static final String DEFAULT_PLAYER = "music_default_player"; 
		public static final String ARTWORK_ENABLED = "music_artwork_enabled"; 
		public static final String ARTWORK_SIZE = "music_artwork_size"; 
		public static final String ARTWORK_PADDING = "music_artwork_padding"; 
	}
	
	public static final class Shortcuts {
		public static final String ENABLE_SHORTCUTS = "shortcuts_enable"; 
		public static final String ENABLE_SHORTCUTS_TITLE = "shortcuts_title_show"; 
		public static final String SHORTCUTS_SIZE = "shortcuts_size"; 
		public static final String SHORTCUTS_TITLE_SIZE = "shortcuts_title_size"; 
		public static final String SHORTCUTS_ORDER = "shortcuts_order"; 
		public static final String SHORTCUTS_DISTANCE = "shortcuts_distance"; 
		public static final String SHORTCUTS_PADDING = "shortcuts_padding"; 
		public static final String SHORTCUTS_EXTERNAL_ICON_PACK = "shortcuts_external_icon_pack";
	}
	
	public static final class PanelsOrder {
		public static final String PANELS_ORDER = "panels_order"; 
	}
	
	public static final class ExternalResources {
		public static final String EXTERNAL_RESOURCE_PACKAGE = "external_resource_package"; 
	}

	public static final class General {
		public static final String PANELS_OFFSET = "panels_offset"; 
		public static final String PANELS_OFFSET_LANDSCAPE = "panels_offset_landscape"; 
		public static final String FORCE_FORGEROUND = "force_foreground"; 
		public static final String PANELS_MARGIN = "panels_margin"; 
		public static final String PANEL_SPAN = "panels_span"; 
		public static final String PANEL_SPAN_LANDSCAPE = "panels_span_landscape"; 
		public static final String DIM_AMOUNT = "dim_amount"; 
		public static final String PANELS_SAME_LAYOUT_LANDSCAPE = "panels_same_layout_landscape"; 
		public static final String DRAG_HANDLER_WIDTH = "panel_drag_handler_width"; 
		public static final String CLOSE_ON_SPAN_TOUCH = "close_on_span_touch"; 
	}
	
	public static final class Notifications {
		public static final String NOTIFICATIONS_ENABLE = "notifications_enabled"; 
		public static final String NOTIFICATIONS_HIDE_QCP_FOREGROUND = "notifications_hide_foreground"; 
		public static final String NOTIFICATIONS_IGNORE_ONGOING = "notifications_hide_ongoing"; 
		public static final String NOTIFICATIONS_SHOW_TOAST = "notifications_show_toast"; 
		public static final String NOTIFICATIONS_IGNORE_SET = "notifications_ignore_set"; 
	}
	
	public static final class Backup {
		public static final String BACKUP_NAME = "backup_backup_name"; 
		public static final String BACKUP_RESTORE_NAME = "backup_restore_name"; 
	}

	public static final class Debug {
		public static final String DEBUG_SWIPE_DETECTOR_VISIBLE = "debug_swipe_detector_visible"; 
	}

}
