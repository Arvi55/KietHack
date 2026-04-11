<template>
  <header
    class="fixed inset-x-0 top-0 z-[100] transition-all duration-300"
    :class="
      isSolid
        ? 'border-b border-forest/10 bg-cream/92 shadow-md shadow-forest/10 backdrop-blur-xl'
        : 'border-b border-transparent bg-transparent'
    "
  >
    <nav
      class="mx-auto flex max-w-7xl items-center justify-between px-4 py-4 sm:px-6 lg:px-8"
      aria-label="Primary"
    >
      <router-link
        to="/"
        class="font-serif text-xl tracking-tight transition-opacity hover:opacity-90 sm:text-2xl"
        :class="[
          isSolid ? 'text-forest' : 'text-sage drop-shadow-md'
        ]"
      >
        Emotion Buddy
      </router-link>
      <div class="flex items-center gap-2 sm:gap-4">
        <router-link
          to="/"
          class="nav-link text-sm font-medium transition-colors sm:text-base"
          :class="isSolid ? 'text-forest hover:text-forest/75' : 'text-sage drop-shadow-md hover:text-cream/80'"
        >
          Home
        </router-link>
        <router-link
          to="/dashboard"
          class="nav-link text-sm font-medium transition-colors sm:text-base"
          :class="isSolid ? 'text-forest hover:text-forest/75' : 'text-sage drop-shadow-md hover:text-cream/80'"
        >
          Dashboard
        </router-link>

        <router-link
          to="/login"
          class="nav-btn rounded-full px-4 py-2 text-sm font-medium shadow-sm transition-colors sm:px-5 sm:text-base ml-2"
          :class="
            isSolid
              ? 'border border-forest/20 bg-white/70 text-forest hover:border-lime/40 hover:bg-sand/80'
              : 'border border-cream/55 bg-black/25 text-cream backdrop-blur-md hover:bg-black/35 hover:border-cream/80'
          "
        >
          Login
        </router-link>
        <router-link
          to="/signup"
          class="nav-btn rounded-full px-4 py-2 text-sm font-medium shadow-md transition hover:brightness-95 sm:px-5 sm:text-base"
          :class="
            isSolid
              ? 'border border-lime/30 bg-lime text-cream hover:brightness-95'
              : 'border border-lime/40 bg-lime text-cream backdrop-blur-sm'
          "
        >
          Sign up
        </router-link>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const navScrolled = ref(false)

function onScroll() {
  navScrolled.value = window.scrollY > 16
}

onMounted(() => {
  window.addEventListener('scroll', onScroll, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
})

const isSolid = computed(() => {
  return route.path !== '/' || navScrolled.value
})
</script>

<style scoped>
/* Header entrance animation */
header {
  animation: slideDown 0.6s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

@keyframes slideDown {
  from {
    transform: translateY(-100%);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

/* Nav Link Hover Animation */
.nav-link {
  position: relative;
  display: inline-block;
  transition: all 0.3s ease;
}

.nav-link::after {
  content: '';
  position: absolute;
  width: 100%;
  transform: scaleX(0);
  height: 2px;
  bottom: -4px;
  left: 0;
  background-color: #b8695e;
  transform-origin: bottom right;
  transition: transform 0.3s cubic-bezier(0.86, 0, 0.07, 1);
}

.nav-link:hover::after {
  transform: scaleX(1);
  transform-origin: bottom left;
}

.nav-link:hover {
  transform: translateY(-1px);
}

/* Indicator for the active page */
.nav-link.router-link-exact-active::after {
  transform: scaleX(1);
}

.nav-link.router-link-exact-active {
  color: #b8695e !important; /* Rose color for active text */
  font-weight: 700;
}

/* Button Animations */
.nav-btn {
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.nav-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.12);
}

.nav-btn.router-link-exact-active {
  box-shadow: 0 0 0 2px #b8695e;
  border-color: #b8695e !important;
}
</style>
