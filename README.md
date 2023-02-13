# PageStateManager
A PageStateManager for Android.
# Screenshot
![screenshot](https://raw.githubusercontent.com/mminng/PageStateManager/master/screenshots/Screenshot_1.png)
# Getting Started
**Step 1.** Add it in your root build.gradle at the end of repositories:
```Groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
**Step 2.** Add the dependency:
```Groovy
dependencies {
    ...
    implementation 'com.github.mminng:PageStateManager:1.0.0'
}
```
### Simple
```Kotlin
val pageManager = PageStateManager.Builder(target = Activity or Fragment or View)
            .setLoadingLayout(loading layout)
            .setEmptyLayout(
                layoutId = empty layout,
                iconId = icon view Id/*support ImageView only*/,
                textId = message view Id/*support TextView only*/,
                clickId = reload click view Id
            ).setErrorLayout(
                layoutId = error layout,
                iconId = icon view Id/*support ImageView only*/,
                textId = message view Id/*support TextView only*/,
                clickId = reload click view Id
            ).setCustomLayout(
                layoutId = custom layout,
                clickId = reload click view Id
            ).setPageCreateListener {
                pageLoadingCreated { view ->
                    //Loading view created
                }
                pageEmptyCreated { view ->
                    //Empty view created
                }
                pageErrorCreated { view ->
                    //Error view created
                }
                pageCustomCreated { view ->
                    //Custom view created
                }
            }.setPageChangeListener {
                pageLoadingChanged { visible, view ->
                    //Loading view changed
                }
                pageEmptyChanged { visible, view ->
                    //Empty view changed
                }
                pageErrorChanged { visible, view ->
                    //Error view changed
                }
                pageCustomChanged { visible, view ->
                    //Custom view changed
                }
            }.build()

pageManager.setReloadListener {
    //reload
}

//If loading page delay 500ms was not shown yet,it will just never be shown.
//If loading page is shown, but not long enough,so delay 500ms to hide it.
pageManager.showLoading()

pageManager.showContent()

pageManager.showEmpty()

pageManager.showError()

pageManager.showCustom()
```
# License
```markdown
Copyright 2022 mminng

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

