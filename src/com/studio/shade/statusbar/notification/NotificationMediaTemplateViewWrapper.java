/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.studio.shade.statusbar.notification;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.view.View;

import com.studio.shade.statusbar.ExpandableNotificationRow;
import com.studio.shade.statusbar.TransformableView;

/**
 * Wraps a notification containing a media template
 */
public class NotificationMediaTemplateViewWrapper extends NotificationTemplateViewWrapper {

    protected NotificationMediaTemplateViewWrapper(Context ctx, View view,
            ExpandableNotificationRow row) {
        super(ctx, view, row);
    }

    View mActions;

    private void resolveViews(StatusBarNotification notification) {
        mActions = mView.findViewById(com.android.internal.R.id.media_actions);
    }

    @Override
    public void notifyContentUpdated(StatusBarNotification notification) {
        // Reinspect the notification. Before the super call, because the super call also updates
        // the transformation types and we need to have our values set by then.
        resolveViews(notification);
        super.notifyContentUpdated(notification);
    }

    @Override
    protected void updateTransformedTypes() {
        // This also clears the existing types
        super.updateTransformedTypes();
        if (mActions != null) {
            mTransformationHelper.addTransformedView(TransformableView.TRANSFORMING_VIEW_ACTIONS,
                    mActions);
        }
    }
}
