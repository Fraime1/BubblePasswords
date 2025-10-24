# ✨ Новые возможности / New Features

## 🇷🇺 Русский

### 🌟 Избранное (Favorites)
- **Добавление в избранное** - долгим нажатием на пароль в детальном экране
- **Визуальное отличие** - избранные пузыри светятся (glow эффект)
- **Фильтрация** - кнопка "Favorites" на главном экране показывает только избранные пароли
- **Иконка сердца** - ❤️ заполненное сердце для избранных, ♡ пустое для обычных

### 🎨 Переключение темы
- **Тёмная тема (Bubble Glow)** - по умолчанию
  - Глубокий фиолетово-чёрный градиент (#0D0D1A → #1A0D2E)
  - Яркие розовые пузыри
  - Голубое свечение
  
- **Светлая тема**
  - Нежный градиент (#E8D5F2 → #FCE4EC)
  - Розовые пузыри на светлом фоне
  - Чёрный текст для читабельности

- **Переключатель в настройках** - Settings → Appearance → Theme toggle

### 🔍 Улучшенная фильтрация
- **"All"** - все пароли
- **"Most used"** - сортировка по частоте использования (useCount)
- **"Favorites"** - только избранные пароли

---

## 🇬🇧 English

### 🌟 Favorites
- **Add to favorites** - tap the favorite button in password details screen
- **Visual distinction** - favorite bubbles have a glow effect
- **Filtering** - "Favorites" button on home screen shows only favorited passwords
- **Heart icon** - ❤️ filled heart for favorites, ♡ empty for regular

### 🎨 Theme Switching
- **Dark Theme (Bubble Glow)** - default
  - Deep purple-black gradient (#0D0D1A → #1A0D2E)
  - Bright pink bubbles
  - Cyan glow effect
  
- **Light Theme**
  - Soft gradient (#E8D5F2 → #FCE4EC)
  - Pink bubbles on light background
  - Black text for readability

- **Toggle in settings** - Settings → Appearance → Theme switch

### 🔍 Enhanced Filtering
- **"All"** - all passwords
- **"Most used"** - sorted by usage count (useCount)
- **"Favorites"** - only favorited passwords

---

## 🔧 Technical Details

### Database Changes
- **VaultItem** entity now includes:
  - `favorite: Boolean = false` - marks item as favorite
  - `useCount: Int = 0` - tracks usage frequency
- Database version updated to **v2** with destructive migration

### New Components
- **PreferencesManager** - DataStore-based settings manager
  - `isDarkTheme: Flow<Boolean>`
  - `isBiometricEnabled: Flow<Boolean>`
  - `isCloudSyncEnabled: Flow<Boolean>`

### UI Updates
- **DetailScreen** - added favorite toggle button with heart icon
- **HomeScreen** - filtering logic for All/Most Used/Favorites
- **SettingsScreen** - theme switcher with live preview
- **MainActivity** - reactive theme switching based on preferences

### Dependencies
- `androidx.datastore:datastore-preferences:1.1.1` - for persistent settings

---

## 📝 Usage Examples

### Adding to Favorites
1. Open any password from the home screen
2. Tap the "Add to Favorites" button (heart icon)
3. The password bubble will now glow on the home screen
4. Use the "Favorites" filter to see only favorited items

### Switching Theme
1. Go to Settings (bottom navigation)
2. Scroll to "Appearance" section
3. Toggle the Theme switch
4. The app will immediately update with new colors
5. Your choice is saved and persists across app restarts

### Filtering Passwords
1. On the home screen, tap any of the filter chips:
   - **All** - shows everything
   - **Most used** - sorts by usage (most used first)
   - **Favorites** - shows only favorited items (with glow)
2. Use the search bar to filter by service name
3. Filters work together with search

---

## 🎯 Coming Soon

- [ ] Biometric lock (Face ID / Touch ID) on app launch
- [ ] Cloud sync for backups
- [ ] Password strength analysis
- [ ] Auto-fill integration
- [ ] Export/Import functionality
- [ ] Custom bubble colors per password


