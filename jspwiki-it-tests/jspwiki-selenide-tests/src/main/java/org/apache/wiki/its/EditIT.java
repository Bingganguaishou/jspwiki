/*
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
 */
package org.apache.wiki.its;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import org.apache.wiki.pages.haddock.EditWikiPage;
import org.apache.wiki.pages.haddock.ReadWikiPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Edit-related tests for Apache JSPWiki
 */
@ExtendWith( ScreenShooterExtension.class )
public class EditIT {

    @Test
    void createPageAndTestEditPermissions() {
        final String pageName = "RandomPage" + System.currentTimeMillis();

        final ReadWikiPage randomPage = EditWikiPage.open( pageName )
                                                    .saveText( "random page [{ALLOW edit janne}] [{ALLOW view All}]", "random page" );
        Assertions.assertEquals( pageName, randomPage.wikiTitle() );
        Assertions.assertEquals( "random page", randomPage.wikiPageContent() );

        final ReadWikiPage requiresJannesAccess = randomPage.hoverLoginArea().clickOnLogin().performLogin();
        requiresJannesAccess.editPage().saveText( "random page [{ALLOW edit janne}]", "random page" );
        Assertions.assertEquals( pageName, requiresJannesAccess.wikiTitle() );
        Assertions.assertEquals( "random page", requiresJannesAccess.wikiPageContent() );

        requiresJannesAccess.hoverLoginArea().logout();
        Assertions.assertEquals( "Main", requiresJannesAccess.wikiTitle() ); // no access for anonymous user, so redirected to main
        Assertions.assertNotEquals( "random page", randomPage.wikiPageContent() );
    }

}
